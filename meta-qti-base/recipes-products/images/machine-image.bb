inherit core-image

include automotive-image.inc

IMAGE_LINGUAS = ""

DEPENDS += " mkbootimg-native ext4-utils-native "

EXTRA_IMAGECMD_ext4 = "-i 4096 -b 4096"

# default value for rootfs size
IMAGE_ROOTFS_SIZE ?= "1572864"

SSTATE_MANFILEPREFIX="${@bb.utils.contains('PERF_BUILD', '1', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}-perf', '${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}' , d)}"

SDK_DEPLOY = "${DEPLOY_DIR}/sdk-${PRODUCT}"

license_create_manifest() {
}

python __anonymous () {
    d.appendVarFlag('do_rootfs', 'depends', ' machine-kdump-image:do_image_complete')
}

ROOTFS_POSTPROCESS_COMMAND_prepend = "${@bb.utils.contains('DISTRO_FEATURES', 'kdump-support', ' add_kdump_ramdisk; ', '', d)}"

add_kdump_ramdisk() {
   cp ${DEPLOY_DIR_IMAGE}/machine-kdump-image-${PRODUCT}.cpio.gz ${IMAGE_ROOTFS}/boot/${BASEMACHINE}.cpio.gz
}
