inherit autotools pkgconfig

DESCRIPTION = "Android IPC utilities"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

DEPENDS = "binder liblog libcutils libhardware libhardware-headers libselinux glib-2.0 system-core-headers"
DEPENDS += " ${@oe.utils.version_less_or_equal('PREFERRED_VERSION_linux-msm', '4.4', '', 'libsync', d)}"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1"
S = "${WORKDIR}/frameworks/libui"
SRCREV = "${AUTOREV}"

EXTRA_OECONF = "--with-glib"

CFLAGS += "-I${STAGING_INCDIR}/libselinux"

CPPFLAGS += "-fpermissive"
LDFLAGS  += "-llog"

FILES_${PN}-libui-dbg    = "${libdir}/.debug/libui.*"
FILES_${PN}-libui        = "${libdir}/libui.so.*"
FILES_${PN}-libbui-dev    = "${libdir}/libui.so ${libdir}/libui.la ${includedir}"
