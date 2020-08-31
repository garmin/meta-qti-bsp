SUMMARY = "QTI package group for extra functions. TBD"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-extra \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    python3 \ 
    resize-service \
    openssl \
    libxml2 \
    libnl \
    coreutils \
    powerapp-reboot \
    powerapp-shutdown \
    sec-config \
    libsensors \
    "
