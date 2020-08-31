SUMMARY = "QTI package group for vichle network"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-vnw \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    open-avb \
    ${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', 'ptp-virtual', '', d)} \
    hsi2s \
    hsi2s-test \
    "
