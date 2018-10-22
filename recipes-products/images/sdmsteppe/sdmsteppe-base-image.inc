require ${COREBASE}/meta-qti-bsp/recipes-products/images/common/common-base-image.inc

#compile abl
DEPENDS  += "${@bb.utils.contains('MULTILIB_VARIANTS', 'lib64','lib32-edk2','edk2',d)}"

#install userspace packages
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "binder"
IMAGE_INSTALL += "av-frameworks"
IMAGE_INSTALL += "media-headers"
#IMAGE_INSTALL += "start-scripts-firmware-links"
#IMAGE_INSTALL += "system-conf"
#IMAGE_INSTALL += "system-prop"
