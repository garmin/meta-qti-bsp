SUMMARY = "QTI package group for security"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-security \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    libcap \
    libcap-bin \
    attr \
    "
