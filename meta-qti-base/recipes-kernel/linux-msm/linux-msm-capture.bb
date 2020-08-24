require recipes-kernel/linux-msm/linux-msm-capture.inc

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

# TEMP: Disable IPA3 config for sdmsteppe
SRC_URI_append = "${@bb.utils.contains('DISTRO_FEATURES', 'ipa3', '', ' file://disableipa3.cfg', d)}"

COMPATIBLE_MACHINE = "(${BASEMACHINE}|sa8195p)"
KERNEL_IMAGEDEST = "boot"
KERNEL_PACKAGE_NAME = "kdump"

SRC_DIR   =  "${SRC_DIR_ROOT}/kernel/msm-4.14"
S         =  "${WORKDIR}/kernel/msm-4.14"
PKGDATA_DIR = "${TMPDIR}/pkgdata/${MACHINE}-kdump"
PR = "r0"

DEPENDS += "dtc-native"
DEPENDS += "${@bb.utils.contains('MACHINE_FEATURES', 'dt-overlay', 'mkdtimg-native', '', d)}"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"
TARGET_CXXFLAGS += "-Wno-format"
EXTRA_OEMAKE_append += "INSTALL_MOD_STRIP=1"
KERNEL_EXTRA_ARGS += "DTC_EXT=${STAGING_BINDIR_NATIVE}/dtc"

do_kernel_metadata_prepend() {
    set +e
    if [ -n "${KBUILD_DEFCONFIG}" ]; then
        if [ -f "${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG}" ]; then
            if [ -f "${WORKDIR}/defconfig" ]; then
                # If the two defconfig's are different, warn that we didn't overwrite the
                # one already placed in WORKDIR by the fetcher.
                cmp "${WORKDIR}/defconfig" "${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG}"
                if [ $? -ne 0 ]; then
                    bbwarn "defconfig detected in WORKDIR. ${KBUILD_DEFCONFIG} overide"
                    cp -f ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${WORKDIR}/defconfig
                fi
            fi
        fi
    fi
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

        # Generate kernel headers
        oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${STAGING_KERNEL_BUILDDIR}
}

nand_boot_flag = "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', '1', '0', d)}"

do_shared_workdir[dirs] = "${DEPLOYDIR}"

INHIBIT_PACKAGE_STRIP = "1"
KERNEL_VERSION_SANITY_SKIP="1"
