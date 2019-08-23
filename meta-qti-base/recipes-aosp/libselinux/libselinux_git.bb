inherit autotools-brokensep pkgconfig

DESCRIPTION = "Libselinux"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://NOTICE;md5=84b4d2c6ef954a2d4081e775a270d0d0"

PR = "r0"

DEPENDS = "libpcre libmincrypt liblog libcutils"

SRC_URI = "${PATH_TO_REPO}/external/libselinux/.git;protocol=${PROTO};destsuffix=external/libselinux;usehead=1"
S = "${WORKDIR}/external/libselinux"

SRCREV = "${AUTOREV}"

EXTRA_OECONF = " --with-pcre"
