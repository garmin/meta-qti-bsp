# Rootfs creation.
IMAGE_INSTALL += "packagegroup-core-boot"
#IMAGE_INSTALL += "${ROOTFS_PKGMANAGE_BOOTSTRAP}"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"

# The MSM Linux minimal boot image files.
IMAGE_INSTALL += "base-files"
IMAGE_INSTALL += "base-passwd"
IMAGE_INSTALL += "glib-2.0"
IMAGE_INSTALL += "start-scripts-find-partitions"
IMAGE_INSTALL += "start-scripts-firmware-links"
IMAGE_INSTALL += "system-core-adbd"
IMAGE_INSTALL += "system-core-usb"
IMAGE_INSTALL += "volatile-binds"

