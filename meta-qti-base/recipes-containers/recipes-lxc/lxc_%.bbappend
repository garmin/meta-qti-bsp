# Fetch code from github
SRCREV = "eaf3c66b93102dd7c093b942443407fbb1a6445f"
SRC_URI_remove= "http://linuxcontainers.org/downloads/${BPN}-${PV}.tar.gz"
SRC_URI_append = "git://github.com/lxc/${BPN}.git;protocol=http;branch=stable-4.0 \
    "
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append += "\
        file://0001-try-to-fix-lxc-4.0.2.patch \
        file://0002-lxc-support-deny-device-by-devpth.patch \
        "
S = "${WORKDIR}/git"

# Enable container launching automatically
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

# Skip wget as license conflicts
RDEPENDS_${PN}_remove = " wget "
