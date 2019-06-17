inherit qimage

include ${BASEMACHINE}/${BASEMACHINE}-${PRODUCT}-image.inc

IMAGE_LINGUAS = ""

DEPENDS += " mkbootimg-native ext4-utils-native "

EXTRA_IMAGECMD_ext4 = "-i 4096 -b 4096"

# default value for rootfs size
IMAGE_ROOTFS_SIZE ?= "1572864"

SSTATE_MANFILEPREFIX="${@bb.utils.contains('PERF_BUILD', '1', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}-perf', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}' , d)}"
