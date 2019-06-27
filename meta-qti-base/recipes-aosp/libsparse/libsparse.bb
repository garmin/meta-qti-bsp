inherit autotools pkgconfig

DESCRIPTION = "Build Android libsprase"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r0"

DEPENDS += "zlib"

SRC_URI   =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core/libsparse;subpath=libsparse;nobranch=1"

S = "${WORKDIR}/system/core/libsparse"

SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/system/core', d)}"

BBCLASSEXTEND = "native"

EXTRA_OECONF_append_class-native = "  --enable-img-convert-utils"
