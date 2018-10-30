require ${COREBASE}/meta-qti-bsp/recipes-products/images/common/common-base-image.inc

#compile abl
RDEPENDS  += "edk2"

#install userspace packages
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "binder"
IMAGE_INSTALL += "av-frameworks"
IMAGE_INSTALL += "media-headers"
IMAGE_INSTALL += "coreutils"
IMAGE_INSTALL += "kernel-modules"
IMAGE_INSTALL += "libion"
#IMAGE_INSTALL += "system-media"
#IMAGE_INSTALL += "start-scripts-firmware-links"
#IMAGE_INSTALL += "system-conf"
#IMAGE_INSTALL += "system-prop"
