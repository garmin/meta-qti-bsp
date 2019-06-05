SUMMARY = "QTI package group for data service"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-data \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    bridge-utils \
    connman \
    ethtool \
    iperf3 \
    iproute2 \
    iproute2-ss \
    iproute2-tc \
    tcpdump \
    vlan \
    "
