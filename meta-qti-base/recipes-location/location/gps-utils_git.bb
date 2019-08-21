inherit autotools-brokensep qcommon pkgconfig

DESCRIPTION = "GPS Utils"
PR = "r1"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/gps/.git;protocol=${PROTO};destsuffix=hardware/qcom/gps/utils;subpath=utils;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/hardware/qcom/gps/utils"

DEPENDS = "glib-2.0 libcutils loc-pla-hdr location-api-iface"
EXTRA_OECONF = "--with-locationapi-includes=${STAGING_INCDIR}/location-api-iface \
                --with-locpla-includes=${STAGING_INCDIR}/loc-pla \
                --with-glib"
