inherit native autotools pkgconfig

DESCRIPTION = "Minimalistic encryption library from Android"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

SRC_URI   =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/system/core/libmincrypt"

EXTRA_OECONF = " --with-core-includes=${WORKDIR}/system/core/include"
