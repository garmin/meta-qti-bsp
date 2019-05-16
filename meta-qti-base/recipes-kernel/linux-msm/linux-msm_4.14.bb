require recipes-kernel/linux-msm/linux-msm.inc

inherit sdllvm

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

# TEMP: Disable IPA3 config for sdmsteppe
SRC_URI_append_sdmsteppe = "file://disableipa3.cfg"

COMPATIBLE_MACHINE = "(${BASEMACHINE})"
KERNEL_IMAGEDEST = "boot"

SRC_DIR   =  "${WORKSPACE}/kernel/msm-4.14"
S         =  "${WORKDIR}/kernel/msm-4.14"
PR = "r0"

DEPENDS += "dtc-native llvm-arm-toolchain-native"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'mkdtimg-native', '', d)}"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"
TARGET_CXXFLAGS += "-Wno-format"
EXTRA_OEMAKE_append += "INSTALL_MOD_STRIP=1"
KERNEL_EXTRA_ARGS += "${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'DTC_EXT=${STAGING_BINDIR_NATIVE}/dtc CONFIG_BUILD_ARM64_DT_OVERLAY=y', '', d)}"

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
                scripts/recordmcount \
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

        # Copy vmlinux and zImage into deplydir for boot.img creation
        install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}
        install -m 0644 vmlinux ${DEPLOY_DIR_IMAGE}

        # Generate kernel headers
        oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${STAGING_KERNEL_BUILDDIR}
}

do_deploy_append() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'true', 'false', d)}; then
        ${STAGING_BINDIR_NATIVE}/mkdtimg create ${DEPLOY_DIR_IMAGE}/dtbo.img ${B}/arch/${ARCH}/boot/dts/qcom/*.dtbo
    fi
}


do_shared_workdir[dirs] = "${DEPLOY_DIR_IMAGE}"

INHIBIT_PACKAGE_STRIP = "1"
