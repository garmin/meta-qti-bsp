SUMMARY = "QTI package group for AOSP packages"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-aosp \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    system-core-adbd \
    system-core-leprop \
    system-core-post-boot \
    system-core-early-boot \
    system-core-usb \
    "
