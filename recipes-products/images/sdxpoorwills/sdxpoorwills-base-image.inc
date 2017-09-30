# Rootfs creation.
IMAGE_INSTALL += "packagegroup-core-boot"
#IMAGE_INSTALL += "${ROOTFS_PKGMANAGE_BOOTSTRAP}"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"

# The MSM Linux minimal boot image files.
IMAGE_INSTALL += "base-files"
IMAGE_INSTALL += "base-passwd"

IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"

# Below is to generate sparse ext4 system image (OE by default supports raw ext4 images)

do_makesystem() {
    make_ext4fs -s -l ${SYSTEM_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${MACHINE}-${PRODUCT}-sysfs.ext4 ${IMAGE_ROOTFS}
    make_ext4fs -s -l ${SYSTEMRW_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${MACHINE}-${PRODUCT}-systemrw.ext4
}
