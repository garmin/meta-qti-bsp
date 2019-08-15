inherit pkgconfig
include wpa-supplicant.inc

PR = "${INC_PR}.2"

SRC_URI   = "${PATH_TO_REPO}/external/wpa_supplicant_8/.git;protocol=${PROTO};destsuffix=external/wpa_supplicant_8;usehead=1"
SRC_URI_append = " file://defconfig-qcacld"
SRCREV = "${AUTOREV}"

DEPENDS += "qmi"
DEPENDS += "qmi-framework"
FILES_${PN} += "/usr/include/*"

S = "${WORKDIR}/external/wpa_supplicant_8/wpa_supplicant"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}

INCSUFFIX ?= "none"
INCSUFFIX_automotive = "wpa-supplicant_auto"
include ${INCSUFFIX}.inc
