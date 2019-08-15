inherit pkgconfig

include hostap-daemon.inc
inherit pkgconfig

PR = "${INC_PR}.2"

SRC_URI   = "${PATH_TO_REPO}/external/wpa_supplicant_8/.git;protocol=${PROTO};destsuffix=external/wpa_supplicant_8;usehead=1"
SRCREV = "${AUTOREV}"

SRC_URI_append = " file://defconfig-qcacld"
DEPENDS = "pkgconfig libnl openssl"

S = "${WORKDIR}/external/wpa_supplicant_8/hostapd/"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}

do_install_append_automotive() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/hostapd.conf ${D}${sysconfdir}/hostapd.conf
}
