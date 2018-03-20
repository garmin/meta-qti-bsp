# Packages to be included in rootfs creation.
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', 'packagegroup-core-boot', '', d)}"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', 'lib64-sectool5', 'sectool5', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', 'lib64-edk2', 'edk2', d)}"

IMAGE_INSTALL += "start-scripts-misc-daemon"

IMAGE_INSTALL += "base-files"
IMAGE_INSTALL += "base-passwd"
IMAGE_INSTALL += "glib-2.0"
IMAGE_INSTALL += "libcutils"
IMAGE_INSTALL += "liblog"
IMAGE_INSTALL += "libxml2"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-logd"
IMAGE_INSTALL += "system-core-leprop"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "system-core-post-boot"
#IMAGE_INSTALL += "system-conf"
#IMAGE_INSTALL += "system-prop"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "volatile-binds"
IMAGE_INSTALL +="${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', 'mtd-utils-ubifs', '', d)}"

# The MSM Linux minimal boot image files.
#IMAGE_INSTALL += "gdbserver"
#IMAGE_INSTALL += "gdb"
#IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "kernel-modules"
IMAGE_INSTALL += "libnl"
IMAGE_INSTALL += "libstdc++"
IMAGE_INSTALL += "openssl"
#IMAGE_INSTALL += "perl"
IMAGE_INSTALL += "powerapp"
IMAGE_INSTALL += "powerapp-powerconfig"
IMAGE_INSTALL += "powerapp-reboot"
IMAGE_INSTALL += "powerapp-shutdown"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES', 'nand-boot', 'start-scripts-find-partitions', '', d)}"
IMAGE_INSTALL += "start-scripts-firmware-links"

do_makesystem() {
    if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
     make_ext4fs -l ${SYSTEM_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${MACHINE}-sysfs.ext4 ${IMAGE_ROOTFS}
     make_ext4fs -l ${SYSTEMRW_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${MACHINE}-systemrw.ext4
    fi
}
