inherit autotools-brokensep pkgconfig

DESCRIPTION = "Pluseaudio codec control module"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
PR = "r0"

DEPENDS = "glib-2.0 pulseaudio audio-hal-plugin-noship"
RDEPENDS_${PN} = "pulseaudio-server pulseaudio-misc pulseaudio-module-null-source"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:"
SRC_URI = "file://vendor/qcom/opensource/mm-audio/pulseaudio-module-codec-control/"
S = "${WORKDIR}/vendor/qcom/opensource/mm-audio/pulseaudio-module-codec-control/"

AUDIO_BUILD_TARGET ?= "sa8155"
EXTRA_OECONF += "--enable-target=${AUDIO_BUILD_TARGET}"
EXTRA_OECONF += "--with-glib"

FILES_${PN} += "${libdir}/pulse-12.2/modules/"
FILES_${PN}-staticdev += "${libdir}/pulse-12.2/modules/*.a"
FILES_${PN}-dbg += "${libdir}/pulse-12.2/modules/.debug"
