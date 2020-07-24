SUMMARY = "lxc delta files for overlayfs."

IMAGE_LINGUAS = " "

LICENSE = "BSD-3-Clause"

inherit image

CONVERSIONTYPES = " "

IMAGE_INSTALL_remove +=" packagegroup-core-boot"
IMAGE_INSTALL_remove +=" packagegroup-base-extended"
PACKAGE_INSTALL_remove +=" run-postinsts opkg"

IMAGE_INSTALL += " packagegroup-qti-lxc"

IMAGE_PREPROCESS_COMMAND_append = " selinux_set_labels ;"

selinux_set_labels () {
    setfattr -n security.selinux -v u:object_r:qti_init_shell_exec:s0 ${IMAGE_ROOTFS}/var/lib/lxc/android1/vendor/bin/init.qti.container.network.sh
    setfattr -n security.selinux -v u:object_r:qti_init_shell_exec:s0 ${IMAGE_ROOTFS}/var/lib/lxc/android2/vendor/bin/init.qti.container.network.sh
    setfattr -n security.selinux -v u:object_r:vendor_configs_file:s0 ${IMAGE_ROOTFS}/var/lib/lxc/android1/vendor/etc/init/hw/init.qti.container.rc
    setfattr -n security.selinux -v u:object_r:vendor_configs_file:s0 ${IMAGE_ROOTFS}/var/lib/lxc/android2/vendor/etc/init/hw/init.qti.container.rc
}
