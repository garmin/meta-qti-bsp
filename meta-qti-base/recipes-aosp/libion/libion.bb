inherit autotools-brokensep pkgconfig

DESCRIPTION = "Build Android libion"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

SRC_URI   =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core/libion;subpath=libion;usehead=1"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/system/core/libion"
DEPENDS += "virtual/kernel liblog"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

EXTRA_OECONF += " --disable-static"
EXTRA_OECONF += "${@bb.utils.contains_any('PREFERRED_VERSION_linux-msm', '3.18 4.4 4.9', '--with-legacyion', '', d)}"
EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

PACKAGES +="${PN}-test-bin"

FILES_${PN}     = "${libdir}/pkgconfig/* ${libdir}/* ${sysconfdir}/*"
FILES_${PN}-test-bin = "${base_bindir}/*"

do_install_append() {
    install -d ${D}${includedir}/kernel-headers
    cp -r ${S}/kernel-headers/*  ${D}${includedir}/kernel-headers
}
