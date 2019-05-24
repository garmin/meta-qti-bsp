#require ../common-image.inc

# Assign a larger rootfs size for sa8155qdrive  platforms
IMAGE_ROOTFS_SIZE = "1572864"

IMAGE_INSTALL += " \
    packagegroup-qti-core-minimal \
    packagegroup-qti-bluetooth \
    packagegroup-qti-data \
    packagegroup-qti-display \
    packagegroup-qti-extra \
    packagegroup-qti-multimedia\
    packagegroup-qti-security \
    packagegroup-qti-vnw \
    "

IMAGE_INSTALL += "packagegroup-ros-qti"
IMAGE_INSTALL += "opkg"
IMAGE_INSTALL += "opkg-utils"
IMAGE_INSTALL += "dropbear"
