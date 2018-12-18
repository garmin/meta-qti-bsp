inherit kernel qperf kernel-yocto

DESCRIPTION = "CAF Linux Kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

python __anonymous () {
  # Override KERNEL_IMAGETYPE_FOR_MAKE variable, which is internal
  # to kernel.bbclass. We override the variable as msm kernel can't
  # support alternate image builds
  if d.getVar("KERNEL_IMAGETYPE", True):
      d.setVar("KERNEL_IMAGETYPE_FOR_MAKE", "")
}

DEPENDS += " mkbootimg-native openssl-native kern-tools-native"
RDEPENDS_kernel-base = ""

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"

DEPENDS_append_aarch64 = " libgcc"
KERNEL_CC_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"
KERNEL_LD_append_aarch64 = " ${TOOLCHAIN_OPTIONS}"

KERNEL_PRIORITY           = "9001"
# Add V=1 to KERNEL_EXTRA_ARGS for verbose
KERNEL_EXTRA_ARGS        += "O=${B}"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   =  "file://kernel \
	      ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'file://systemd.cfg', '', d)} \
              ${@bb.utils.contains('DISTRO_FEATURES', 'virtualization', 'file://virtualization.cfg', '', d)} \
              ${@bb.utils.contains('DISTRO_FEATURES', 'nand-squashfs', 'file://squashfs.cfg', '', d)} \
	     "

PACKAGE_ARCH = "${MACHINE_ARCH}"
PACKAGES = "kernel kernel-base kernel-vmlinux kernel-dev kernel-modules"

KBRANCH ?= ""
KMETA = "kernel-meta"
KMACHINE ?= "${MACHINE}"
KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG ?= "${KERNEL_CONFIG}"
LINUX_VERSION_EXTENSION = "${@['-perf', ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"

do_kernel_checkout[noexec] = "1"

# extra task for configuration checks
addtask kernel_configcheck after do_configure before do_compile

do_compile () {
    oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS} $use_alternate_initrd
}

# keep this path in-sync with bouncycastle recipe.
SECURITY_TOOLS_DIR = "${TMPDIR}/work-shared/security_tools"

do_configure[depends] += "bouncycastle:do_unpack"

# Copy verity certificate into ${S} to generate verity signed boot image
do_configure_append () {
    if [ "${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity', 'dm-verity', '', d)}" = "dm-verity" ] ; then
        openssl x509 -in ${SECURITY_TOOLS_DIR}/verity.x509.pem -outform der -out ${S}/verity.x509
    fi
}

do_deploy() {
    if [[ ${KERNEL_IMAGETYPE} != *-dtb ]]; then
        bberror "${PN}: Only appended DTB supported; Change KERNEL_IMAGETYPE to ${KERNEL_IMAGETYPE}-dtb in your kernel config."
        return
    fi

    if [ -f ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ]; then
        mv ${D}/${KERNEL_IMAGEDEST}/-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
    fi
}

# Put the zImage in the kernel-dev pkg
FILES_kernel-dev += "/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}"
