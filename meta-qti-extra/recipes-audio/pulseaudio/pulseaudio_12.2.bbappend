FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"

SRC_URI += "\
    file://0001-disable-timer-based-scheduling.patch \
    file://0002-default.pa-Load-acdb-and-codec-control-modules.patch \
    file://0007-stream-event-extension.patch \
    file://0008-Pulseaudio-service-need-to-wait-for-sound-card-ready.patch \
    file://0003-default.pa-Load-agl-audio-plugin-module.patch \
    file://0006-Support-PulseAudio-Client-API-for-Module-Codec-Control.patch \
    file://0001-pulseaudio-config-default.pa-to-disable-default-ALSA.patch \
    file://0001-udev-Add-rules-for-QTI-SA8155.patch \
"

RDEPENDS_pulseaudio-server += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'pulseaudio-module-systemd-login', '', d)} \
"
