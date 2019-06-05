inherit autotools-brokensep pkgconfig

DESCRIPTION = "Build Android liblog"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

DEPENDS = "glib-2.0"

SRC_URI   =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=liblog;subpath=liblog;nobranch=1"
SRC_URI_append = " file://50-log.rules"

SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/system/core', d)}"
S = "${WORKDIR}/liblog"

BBCLASSEXTEND = "native"

EXTRA_OECONF += " --with-core-includes=${WORKSPACE}/system/core/include"
EXTRA_OECONF += " --disable-static"

CFLAGS += " -Dstrlcpy=g_strlcpy "
LDFLAGS += " -lglib-2.0 "

do_install_append() {
    if [ "${CLASSOVERRIDE}" = "class-target" ]; then
       install -m 0644 -D ../50-log.rules ${D}${sysconfdir}/udev/rules.d/50-log.rules
    fi
}

FILES_${PN}-dbg = "${libdir}/.debug/* ${bindir}/.debug/*"
FILES_${PN}     = "${libdir}/pkgconfig/* ${libdir}/* ${sysconfdir}/*"
FILES_${PN}-dev = "${libdir}/*.so ${libdir}/*.la ${includedir}/*"
