SUMMARY = "QTI package group for vichle network"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-vnw \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    hsi2s \
    hsi2s-test \
    "
