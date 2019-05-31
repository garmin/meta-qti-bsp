DESCRIPTION = "Audio package group"
LICENSE = "GPL-2.0 & BSD"

inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    init-audio \
    audiodlkm \
    pulseaudio-module-codec-control \
    agl-audio-plugin \
    pulseaudio-module-acdb \
"
