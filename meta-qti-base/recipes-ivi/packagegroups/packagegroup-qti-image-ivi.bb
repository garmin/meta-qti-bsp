SUMMARY = "The middlewares for QTI ivi "

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-image-ivi \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-qti-core-minimal \
    packagegroup-qti-ivi-display \
    packagegroup-qti-ivi-multimedia \
    "
