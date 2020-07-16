SUMMARY = "lxc delta files for overlayfs."

IMAGE_LINGUAS = " "

LICENSE = "BSD-3-Clause"

inherit image

CONVERSIONTYPES = " "

IMAGE_INSTALL_remove +=" packagegroup-core-boot"
IMAGE_INSTALL_remove +=" packagegroup-base-extended"
PACKAGE_INSTALL_remove +=" run-postinsts opkg"

IMAGE_INSTALL += " packagegroup-qti-lxc"
