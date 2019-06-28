inherit native autotools pkgconfig

DESCRIPTION = "EXT4 UTILS"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

DEPENDS = "libselinux-native libsparse-native libcutils-native libpcre-native"

SRC_URI   =  "${PATH_TO_REPO}/system/extras/.git;protocol=${PROTO};destsuffix=system/extras/ext4_utils;subpath=ext4_utils;nobranch=1"
SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/system/extras', d)}"


S = "${WORKDIR}/system/extras/ext4_utils"

CPPFLAGS += "-I${STAGING_INCDIR}/libselinux"
