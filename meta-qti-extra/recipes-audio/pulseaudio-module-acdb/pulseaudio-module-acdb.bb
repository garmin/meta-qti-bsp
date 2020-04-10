inherit autotools-brokensep pkgconfig

DESCRIPTION = "Pluseaudio module for audio calibration data"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
PR = "r0"

DEPENDS = "glib-2.0 pulseaudio acdbloader audcal json-c"
RDEPENDS_${PN} = "acdbloader"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/mm-audio/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/mm-audio/pulseaudio-module-acdb;subpath=pulseaudio-module-acdb;usehead=1"
SRCREV  = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/mm-audio/pulseaudio-module-acdb"

AUDIO_BUILD_TARGET ?= "sa8155"
EXTRA_OECONF += "--enable-target=${AUDIO_BUILD_TARGET}"
EXTRA_OECONF += "--enable-acdbservice=yes"
EXTRA_OECONF += "--with-glib \"

FILES_${PN} += "${libdir}/pulse-12.2/modules/"
FILES_${PN}-staticdev += "${libdir}/pulse-12.2/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-12.2/modules/.debug"

do_install_append() {
         mkdir -p ${D}${sysconfdir}/pulse/
         install -m 0755 ${S}/*.cfg  -D ${D}${sysconfdir}/pulse/
}
