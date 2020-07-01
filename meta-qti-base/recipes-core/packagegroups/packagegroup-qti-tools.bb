SUMMARY = "QTI package group for test"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-tools \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    file \
    pciutils \
    util-linux \
    "
