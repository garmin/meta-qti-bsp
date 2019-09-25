DESCRIPTION = "Android system/core components headers"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI   =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"
S = "${WORKDIR}/system/core/"
SRCREV = "${AUTOREV}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    # export head files
    install -d ${D}${includedir}/system
    install -m 0644 ${S}/include/system/camera.h  ${D}${includedir}/system/
    install -m 0644 ${S}/include/system/graphics.h  ${D}${includedir}/system/
    install -m 0644 ${S}/include/system/thread_defs.h  ${D}${includedir}/system/
    install -m 0644 ${S}/include/system/window.h  ${D}${includedir}/system/

    install -d ${D}${includedir}/sys
    install -m 0644 ${S}/include/sys/system_properties.h  ${D}${includedir}/sys/

    install -d ${D}${includedir}/netutils
    install -m 0644 ${S}/include/netutils/dhcp.h  ${D}${includedir}/netutils/
    install -m 0644 ${S}/include/netutils/ifc.h  ${D}${includedir}/netutils/
}

