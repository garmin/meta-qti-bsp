SUMMARY = "QTI package group for bluetooth"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-bluetooth \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    synergy-opensource \
    csrspp-tty \
    "
