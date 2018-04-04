require recipes-kernel/linux-msm/linux-msm.inc

COMPATIBLE_MACHINE = "(mdm9607|mdm9650|apq8009|apq8096|apq8053|apq8017|msm8909w|sdx20)"

KERNEL_IMAGEDEST_apq8096 = "boot"

SRC_DIR   =  "${WORKSPACE}/kernel/msm-3.18"
S         =  "${WORKDIR}/kernel/msm-3.18"
GITVER    =  "${@base_get_metadata_git_revision('${SRC_DIR}',d)}"
PR = "${@base_conditional('PRODUCT', 'psm', 'r5-psm', 'r5', d)}"

DEPENDS_apq8096 += "dtc-native"

do_shared_workdir_append () {
        cp Makefile $kerneldir/
        cp -fR usr $kerneldir/

        cp include/config/auto.conf $kerneldir/include/config/auto.conf

        if [ -d arch/${ARCH}/include ]; then
                mkdir -p $kerneldir/arch/${ARCH}/include/
                cp -fR arch/${ARCH}/include/* $kerneldir/arch/${ARCH}/include/
        fi

        if [ -d arch/${ARCH}/boot ]; then
                mkdir -p $kerneldir/arch/${ARCH}/boot/
                cp -fR arch/${ARCH}/boot/* $kerneldir/arch/${ARCH}/boot/
        fi

        if [ -d scripts ]; then
            for i in \
                scripts/basic/bin2c \
                scripts/basic/fixdep \
                scripts/conmakehash \
                scripts/dtc/dtc \
                scripts/kallsyms \
                scripts/kconfig/conf \
                scripts/mod/mk_elfconfig \
                scripts/mod/modpost \
                scripts/sign-file \
                scripts/sortextable;
            do
                if [ -e $i ]; then
                    mkdir -p $kerneldir/`dirname $i`
                    cp $i $kerneldir/$i
                fi
            done
        fi

        cp ${STAGING_KERNEL_DIR}/scripts/gen_initramfs_list.sh $kerneldir/scripts/

        # Make vmlinux available as soon as possible
        VMLINUX_DIR=${@base_conditional('PERF_BUILD', '1', '${STAGING_DIR_TARGET}-perf', base_conditional('PRODUCT', 'psm', '${STAGING_DIR_TARGET}-psm', '${STAGING_DIR_TARGET}', d), d)}
        install -d ${VMLINUX_DIR}/${KERNEL_IMAGEDEST}
        install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${VMLINUX_DIR}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
        install -m 0644 vmlinux ${VMLINUX_DIR}/${KERNEL_IMAGEDEST}/vmlinux-${KERNEL_VERSION}
        install -m 0644 vmlinux ${VMLINUX_DIR}/${KERNEL_IMAGEDEST}/vmlinux
}

do_deploy() {
    if [ -f ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ]; then
        mv ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    fi
    if [ ${nand_boot_flag} == "1" ]; then
        dtb_files=`find ${B}/arch/${ARCH}/boot/dts -iname *${KERNEL_DTS_NAME}*.dtb | awk -Fdts/ '{print $NF}' | awk -F[.][d] '{print $1}'`

        # Create separate images with dtb appended to zImage for all targets.
        for d in ${dtb_files}; do
            #Strip qcom from the result if its present.
            targets=`echo ${d#${KERNEL_DTS_NAME}-}| awk '{split($0,a, "/");print a[2]}'`
            #If dtb are stored inside qcom then we need to search for them inside qcom, else inside dts.
            qcom_check=`echo ${d}| awk '{split($0,a, "/");print a[1]}'`
            if [ ${qcom_check} == "qcom" ]; then
                cat ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} ${B}/arch/${ARCH}/boot/dts/${d}.dtb > ${B}/arch/${ARCH}/boot/dts/qcom/dtb-${KERNEL_IMAGETYPE}-${KERNEL_VERSION}-${targets}
                ${STAGING_BINDIR_NATIVE}/dtbtool ${B}/arch/${ARCH}/boot/dts/qcom/ -s ${PAGE_SIZE} -o ${D}/${KERNEL_IMAGEDEST}/masterDTB -p ${B}/scripts/dtc/ -v
            else
                cat ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} ${B}/arch/${ARCH}/boot/dts/${d}.dtb > ${B}/arch/${ARCH}/boot/dts/dtb-${KERNEL_IMAGETYPE}-${KERNEL_VERSION}-${targets}
                ${STAGING_BINDIR_NATIVE}/dtbtool ${B}/arch/${ARCH}/boot/dts/ -s ${PAGE_SIZE} -o ${D}/${KERNEL_IMAGEDEST}/masterDTB -p ${B}/scripts/dtc/ -v
            fi
        done
    fi

    extra_mkbootimg_params=""
    if [ ${nand_boot_flag} == "1" ]; then
        extra_mkbootimg_params='--dt ${D}/${KERNEL_IMAGEDEST}/masterDTB --tags-addr ${KERNEL_TAGS_OFFSET}'
    fi

    mkdir -p ${DEPLOY_DIR_IMAGE}

    # Make bootimage
    ${STAGING_BINDIR_NATIVE}/mkbootimg --kernel ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} \
        --ramdisk /dev/null \
        --cmdline "${KERNEL_CMD_PARAMS}" \
        --pagesize ${PAGE_SIZE} \
        --base ${KERNEL_BASE} \
        --ramdisk_offset 0x0 \
        ${extra_mkbootimg_params} --output ${DEPLOY_DIR_IMAGE}/${BOOTIMAGE_TARGET}
}

