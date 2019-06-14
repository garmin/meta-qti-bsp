inherit qimage

require recipes-products/images/core-image-minimal-prop.bb

DEPENDS += " mkbootimg-native ext4-utils-native "

IMAGE_INSTALL += " \
    packagegroup-qti-core-boot \
    "
