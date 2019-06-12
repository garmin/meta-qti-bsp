SUMMARY = "QTI package group for wlan"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-wlan \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    hostap-daemon-qcacld \
    iw \
    qcacld32-ll-hasting \
    qcacld32-ll-genoa \
    qcacld32-ll-rome \
    wlan-sigma-dut \
    wpa-supplicant \
    "
