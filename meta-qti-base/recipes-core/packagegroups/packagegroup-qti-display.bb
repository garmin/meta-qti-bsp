SUMMARY = "QTI package group for weston"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-display \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    libdrm \
    wayland \
    wayland-ivi-extension \
    weston \
    weston-examples \
    display-hal-linux \
    display-commonsys-intf-linux \
    "
