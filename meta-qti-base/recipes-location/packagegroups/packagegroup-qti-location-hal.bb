SUMMARY = "QTI package group for Location modules"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-location-hal \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    gps-utils \
    "
