inherit kernel qperf

DESCRIPTION = "CAF Linux Kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "(mdm9607|mdm9650|apq8009|apq8096|apq8053|apq8017|msm8909w|sdx20)"

python __anonymous () {
  # Override KERNEL_IMAGETYPE_FOR_MAKE variable, which is internal
  # to kernel.bbclass. We override the variable as msm kernel can't
  # support alternate image builds
  if d.getVar("KERNEL_IMAGETYPE", True):
      d.setVar("KERNEL_IMAGETYPE_FOR_MAKE", "")
}

KERNEL_IMAGEDEST_apq8096 = "boot"

DEPENDS_append_aarch64 = " libgcc"
KERNEL_CC_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"

KERNEL_PRIORITY           = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS        += "O=${B}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   =  "file://kernel"

SRC_DIR   =  "${WORKSPACE}/kernel/msm-3.18"
S         =  "${WORKDIR}/kernel/msm-3.18"
GITVER    =  "${@base_get_metadata_git_revision('${SRC_DIR}',d)}"

PR = "${@base_conditional('PRODUCT', 'psm', 'r5-psm', 'r5', d)}"

DEPENDS += "mkbootimg-native openssl-native"
DEPENDS_apq8096 += "dtc-native"
DEPENDS += " ${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity', 'bouncycastle-native', '', d)}"

RDEPENDS_kernel-base = ""

PACKAGES = "kernel kernel-base kernel-vmlinux kernel-dev kernel-modules"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"

# Put the zImage in the kernel-dev pkg
FILES_kernel-dev += "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}"

# Additional defconfigs for systemd
do_defconfig_patch () {
cat >> ${S}/arch/${ARCH}/configs/${KERNEL_CONFIG} <<KERNEL_EXTRACONFIGS
CONFIG_DEVTMPFS=y
CONFIG_DEVTMPFS_MOUNT=y
CONFIG_FHANDLE=y
KERNEL_EXTRACONFIGS
}

do_patch_append () {
    if bb.utils.contains('DISTRO_FEATURES', 'systemd', True, False, d):
        bb.build.exec_func('do_defconfig_patch',d)
}

do_configure () {
    oe_runmake_call -C ${S} ARCH=${ARCH} ${KERNEL_EXTRA_ARGS} ${KERNEL_CONFIG}
}

do_compile () {
    oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS} $use_alternate_initrd
}

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

do_install_append() {
    oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${STAGING_KERNEL_BUILDDIR}
}

nand_boot_flag = "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', '1', '0', d)}"

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
        ${extra_mkbootimg_params} --output ${DEPLOY_DIR_IMAGE}/${MACHINE}-boot.img
}

# keep this path in-sync with bouncycastle recipe.
SECURITY_TOOLS_DIR = "${TMPDIR}/work-shared/security_tools"

# Copy verity certificate into ${S} to generate verity signed boot image
do_configure_append () {
    if [ "${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity', 'dm-verity', '', d)}" = "dm-verity" ] ; then
        openssl x509 -in ${SECURITY_TOOLS_DIR}/verity.x509.pem -outform der -out ${S}/verity.x509
    fi
}

# Update kernel cmdline for dm-verity.
do_deploy[prefuncs] += "update_cmdline"

python update_cmdline () {
    if bb.utils.contains('DISTRO_FEATURES', 'dm-verity', True, False, d):
        import subprocess

        cmdline = d.getVar('KERNEL_CMD_PARAMS', True)
        cmdline += " androidboot.veritymode=enforcing"
        # add "buildvariant=userdebug" for non-user builds.
        cmdline += " ${@['buildvariant=userdebug', ''][(d.getVar('VARIANT', True) == 'user')]}"
        # generate and add verity key id.
        keycmd = "openssl x509 -in ${SECURITY_TOOLS_DIR}/verity.x509.pem -text \
                         | grep keyid | sed 's/://g' | tr -d '[:space:]' | tr '[:upper:]' '[:lower:]' | sed 's/keyid//g'"
        keyid = subprocess.check_output(keycmd, shell=True).strip()
        cmdline += " veritykeyid=id:" + keyid

        d.setVar('KERNEL_CMD_PARAMS', ''.join(cmdline))
}