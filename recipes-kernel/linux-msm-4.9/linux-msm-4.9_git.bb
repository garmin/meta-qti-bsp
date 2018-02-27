inherit kernel qperf

DESCRIPTION = "CAF Linux Kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "(qcs605|sdxpoorwills)"

python __anonymous () {
  if (d.getVar('PERF_BUILD', True) == '1'):
      imgtype = d.getVar("KERNEL_PERF_IMAGETYPE", True)
      if imgtype:
          d.setVar("KERNEL_IMAGETYPE", d.getVar("KERNEL_PERF_IMAGETYPE", True))
      perfconf = d.getVar("KERNEL_PERF_DEFCONFIG", True)
      if perfconf:
          d.setVar("KERNEL_CONFIG", d.getVar("KERNEL_PERF_DEFCONFIG", True))
      perfcmd = d.getVar("KERNEL_PERF_CMD_PARAMS", True)
      if perfcmd:
          d.setVar("KERNEL_CMD_PARAMS", d.getVar("KERNEL_PERF_CMD_PARAMS", True))
  else:
      d.setVar("KERNEL_CONFIG", d.getVar("KERNEL_DEFCONFIG", True))

  # Override KERNEL_IMAGETYPE_FOR_MAKE variable, which is internal
  # to kernel.bbclass. We override the variable as msm kernel can't
  # support alternate image builds
  if d.getVar("KERNEL_IMAGETYPE", True):
      d.setVar("KERNEL_IMAGETYPE_FOR_MAKE", "")
}

KERNEL_IMAGEDEST = "boot"

DEPENDS_append_aarch64 = " libgcc"
KERNEL_CC_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"

KERNEL_PRIORITY           = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS        += "O=${B}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   =  "file://kernel"

SRC_DIR   =  "${WORKSPACE}/kernel/msm-4.9"
S         =  "${WORKDIR}/kernel/msm-4.9"
GITVER    =  "${@base_get_metadata_git_revision('${SRC_DIR}',d)}"
PV = "git"
PR = "r5"

DEPENDS += "mkbootimg-native dtc-native openssl-native"
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

        # Make vmlinux available as soon as possible
        if [[ ${PERF_BUILD} == "1" ]]; then
		install -d ${STAGING_DIR_TARGET}-perf/${KERNEL_IMAGEDEST}
	        install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${STAGING_DIR_TARGET}-perf/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
	        install -m 0644 vmlinux ${STAGING_DIR_TARGET}-perf/${KERNEL_IMAGEDEST}/vmlinux-${KERNEL_VERSION}
	        install -m 0644 vmlinux ${STAGING_DIR_TARGET}-perf/${KERNEL_IMAGEDEST}/vmlinux
	else
	        install -d ${STAGING_DIR_TARGET}/${KERNEL_IMAGEDEST}
	        install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${STAGING_DIR_TARGET}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
	        install -m 0644 vmlinux ${STAGING_DIR_TARGET}/${KERNEL_IMAGEDEST}/vmlinux-${KERNEL_VERSION}
	        install -m 0644 vmlinux ${STAGING_DIR_TARGET}/${KERNEL_IMAGEDEST}/vmlinux
	fi
}

do_install_append() {
    oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${STAGING_KERNEL_BUILDDIR}
}

nand_boot_flag = "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', '1', '0', d)}"

do_deploy() {
    if [ -f ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ]; then
        mv ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    fi

    extra_mkbootimg_params=""
    if [ ${nand_boot_flag} == "1" ]; then
        extra_mkbootimg_params=' --tags-addr ${KERNEL_TAGS_OFFSET}'
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
