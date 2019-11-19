# Fetch code from github
SRCREV = "4dcd858b92d4135024290d70534c245e9cdd8d6d"

SRC_URI_remove= "http://linuxcontainers.org/downloads/${BPN}-${PV}.tar.gz"
SRC_URI_append = "git://github.com/lxc/${BPN}.git;protocol=http;branch=master \
    "
S = "${WORKDIR}/git"

# Enable container launching automatically
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

# Skip wget as license conflicts
RDEPENDS_${PN}_remove = " wget "