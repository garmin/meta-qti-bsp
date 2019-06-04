inherit autotools-brokensep pkgconfig

DESCRIPTION = "Libselinux"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://NOTICE;md5=84b4d2c6ef954a2d4081e775a270d0d0"

PR = "r0"

DEPENDS = "libpcre libmincrypt liblog libcutils"

SRC_URI = "${PATH_TO_REPO}/external/libselinux/.git;protocol=${PROTO};destsuffix=external/libselinux;nobranch=1"
S = "${WORKDIR}/external/libselinux"

SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/external/libselinux', d)}"

EXTRA_OECONF = " --with-pcre --with-core-includes=${WORKSPACE}/system/core/include"
