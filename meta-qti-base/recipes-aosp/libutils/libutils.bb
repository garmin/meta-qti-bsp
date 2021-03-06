inherit autotools pkgconfig

DESCRIPTION = "Build LE libutils"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

SRC_URI   =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"

S = "${WORKDIR}/system/core/libutils"

SRCREV = "${AUTOREV}"

DEPENDS += "safe-iop"

EXTRA_OECONF += "--with-system-core-includes=${WORKDIR}/system/core/include"
EXTRA_OECONF += "--with-liblog-includes=${WORKDIR}/system/core/liblog"

FILES_${PN}-dbg    = "${libdir}/.debug/libutils.*"
FILES_${PN}        = "${libdir}/libutils.so.* ${libdir}/pkgconfig/*"
FILES_${PN}-dev    = "${libdir}/libutils.so ${libdir}/libutils.la ${includedir}"
