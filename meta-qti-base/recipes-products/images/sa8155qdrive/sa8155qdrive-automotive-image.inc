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
    packagegroup-qti-ros \
    packagegroup-qti-qdrive \
    packagegroup-qti-tools \
    packagegroup-qti-location-hal \
    "
