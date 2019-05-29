SUMMARY = "QTI package group for debug tools and application"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-debug \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    "
