DESCRIPTION = "The minimal set of packages required to boot the system"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-boot \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-core-boot \
    "

DEPENDS += "edk2"

