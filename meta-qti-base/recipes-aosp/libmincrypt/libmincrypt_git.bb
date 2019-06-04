inherit autotools pkgconfig

DESCRIPTION = "Build Android libmincrypt"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r0"

SRC_URI   =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=libmincrypt;subpath=libmincrypt;nobranch=1"

SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/system/core', d)}"
S = "${WORKDIR}/libmincrypt"

EXTRA_OECONF = " --with-core-includes=${WORKSPACE}/system/core/include"

