SUMMARY = "QTI package group for lxc container"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-lxc \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    lxc \
    lxc-conf \
    "
