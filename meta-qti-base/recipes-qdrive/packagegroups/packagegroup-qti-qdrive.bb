SUMMARY = "QTI package group for Qdrive"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-qdrive \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    opkg \
    opkg-utils \
    libusb1 \
    usbutils \
    valgrind\
    sysstat \
    "
