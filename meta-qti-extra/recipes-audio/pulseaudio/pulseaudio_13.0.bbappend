FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"

inherit distro_features_check

# AGL 5.0
SRC_URI += "\
	https://source.codeaurora.org/quic/le/AGL/meta-agl/plain/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0001-install-files-for-a-module-development.patch?h=automotivelinux/eel;downloadfilename=0001-install-files-for-a-module-development.patch;md5sum=0ad43d60adc2746787f2266ac647363d;sha256sum=cbbb1bf93bf3ba4ac4e6f3f1b0a4d83fa8d99d4a044021a6e4c6667257b1e755 \
	https://source.codeaurora.org/quic/le/AGL/meta-agl/plain/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0002-volume-ramp-additions-to-the-low-level-infra.patch?h=automotivelinux/eel;downloadfilename=0002-volume-ramp-additions-to-the-low-level-infra.patch;md5sum=c272d39b46f5bd976b3f8dd04c54f1b1;sha256sum=085d2dd1c778f5a2fd76fd0b7a8a65f15493e0096bde0ddc96d276dda4753c91 \
	https://source.codeaurora.org/quic/le/AGL/meta-agl/plain/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0003-volume-ramp-adding-volume-ramping-to-sink-input.patch?h=automotivelinux/eel;downloadfilename=0003-volume-ramp-adding-volume-ramping-to-sink-input.patch;md5sum=704aae7060c54fae394fbf57eb882855;sha256sum=4f8f0f3693d24cba2c9408550766f660e75aa228ba2c1fa8df0d7ae71ecb1831 \
	https://source.codeaurora.org/quic/le/AGL/meta-agl/plain/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0004-sink-input-Code-cleanup-regarding-volume-ramping.patch?h=automotivelinux/eel;downloadfilename=0004-sink-input-Code-cleanup-regarding-volume-ramping.patch;md5sum=b8c8fd9c9fcda07c09da067633cc8954;sha256sum=727e3b9bf87ffb3fb8eeaafda15cb627731cf366a6d97a141fdfea01f05aac81 \
	https://source.codeaurora.org/quic/le/AGL/meta-agl/plain/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0/0005-sink-input-volume-Add-support-for-volume-ramp-factor.patch?h=automotivelinux/eel;downloadfilename=0005-sink-input-volume-Add-support-for-volume-ramp-factor.patch;md5sum=e714011cf29d1fc02ec5d9157c04e495;sha256sum=493c5179598fe852014e00443795a6e8b0cfdddffe60133027dac449c2730d5a \
"

SRC_URI += "\
    file://0001-disable-timer-based-scheduling.patch \
    file://0002-default.pa-Load-acdb-and-codec-control-modules.patch \
    file://0007-stream-event-extension.patch \
    file://0008-Pulseaudio-service-need-to-wait-for-sound-card-ready.patch \
    file://0003-default.pa-Load-agl-audio-plugin-module.patch \
    file://0006-Support-PulseAudio-Client-API-for-Module-Codec-Control.patch \
    file://0001-pulseaudio-config-default.pa-to-disable-default-ALSA.patch \
"

REQUIRED_DISTRO_FEATURES = "systemd"

RDEPENDS_pulseaudio-server += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'pulseaudio-module-systemd-login', '', d)} \
"

PACKAGES =+ " pulseaudio-module-dev"

FILES_pulseaudio-module-dev = "${includedir}/pulsemodule/* ${libdir}/pkgconfig/pulseaudio-module-devel.pc"
