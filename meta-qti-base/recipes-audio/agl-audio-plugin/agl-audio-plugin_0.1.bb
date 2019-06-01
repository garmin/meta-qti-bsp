SUMMARY = "AGL Audio Policy Plugin"
DESCRIPTION = "AGL PulseAudio Routing plugin, forked from the Tizen IVI \
PulseAudio Routing plugin, also known as module-murphy-ivi. This is a \
stripped-down version of the former, not needing Murphy anymore and using \
either a JSON configuration file or its own embedded configuration."
HOMEPAGE = "http://www.iot.bzh"

LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

DEPENDS = "json-c pulseaudio"
RDEPENDS_${PN} = "pulseaudio-server pulseaudio-module-null-sink pulseaudio-module-loopback"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:"
SRC_URI = "file://vendor/qcom/opensource/agl-audio-plugin/"

S = "${WORKDIR}/vendor/qcom/opensource/agl-audio-plugin/"

inherit cmake pkgconfig

FULL_OPTIMIZATION = "-O1 -pipe ${DEBUG_FLAGS}"

PULSE_PV="12.2"

EXTRA_OECMAKE_append = " -DPULSE_PV:STRING=${PULSE_PV}"

FILES_${PN} += "${libdir}/pulse-${PULSE_PV}/modules/* ${sysconfdir}/pulse/*"
FILES_${PN}-dbg += "${libdir}/pulse-${PULSE_PV}/modules/.debug/*"
