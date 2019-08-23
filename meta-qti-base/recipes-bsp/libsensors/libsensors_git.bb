inherit autotools-brokensep pkgconfig

DESCRIPTION = "Sensor library"
PR = "r1"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/sensors/.git;protocol=${PROTO};destsuffix=hardware/qcom/sensors;usehead=1"
S = "${WORKDIR}/hardware/qcom/sensors"

SRCREV = "${AUTOREV}"
DEPENDS = "glib-2.0"
EXTRA_OECONF = "--with-glib"
