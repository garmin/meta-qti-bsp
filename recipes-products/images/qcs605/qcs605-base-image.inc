# Packages to be included in rootfs creation.
IMAGE_INSTALL += "packagegroup-core-boot"
IMAGE_INSTALL += "${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','selinux', 'packagegroup-core-selinux', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','selinux', 'selinux-config', '', d)}"
IMAGE_INSTALL += "${@bb.utils.contains('DISTRO_FEATURES','selinux', 'refpolicy-mls', '', d)}"

IMAGE_INSTALL += "start-scripts-firmware-links"
IMAGE_INSTALL += "start-scripts-find-partitions"
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
IMAGE_INSTALL += "system-conf"
#IMAGE_INSTALL += "system-prop"
IMAGE_INSTALL += "wireless-tools"
IMAGE_INSTALL += "volatile-binds"

# Below is to generate sparse ext4 system image (OE by default supports raw ext4 images)
SELINUX_FILE_CONTEXTS = "${IMAGE_ROOTFS}/etc/selinux/mls/contexts/files/file_contexts"

IMAGE_EXT4_SELINUX_OPTIONS = "${@bb.utils.contains('DISTRO_FEATURES','selinux',' -a / -S ${SELINUX_FILE_CONTEXTS} ','',d)}"

do_fsconfig_append() {
if [ "${DISTRO_NAME}" == "msm-user" ]; then
sed -i '/mount -t debugfs/ d' ${IMAGE_ROOTFS}/etc/init.d/sysfs.sh
fi
 chmod -R o-rwx ${IMAGE_ROOTFS}/etc/init.d/

}

do_makesystem() {
    make_ext4fs ${IMAGE_EXT4_EXTRA_OPTIONS} -s ${IMAGE_EXT4_SELINUX_OPTIONS} -l ${SYSTEM_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${MACHINE}-sysfs.ext4 ${IMAGE_ROOTFS}
    make_ext4fs  -l ${SYSTEMRW_SIZE_EXT4} ${DEPLOY_DIR_IMAGE}/${MACHINE}-systemrw.ext4
}
