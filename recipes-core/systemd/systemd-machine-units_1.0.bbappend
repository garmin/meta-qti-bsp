FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "${@bb.utils.contains('DISTRO_FEATURES','ab-boot-support','file://set-slotsuffix.service','',d)}"
SRC_URI_append_batcam += " file://pre_hibernate.sh"
SRC_URI_append_batcam += " file://post_hibernate.sh"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    if ${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/set-slotsuffix.service ${D}${systemd_unitdir}/system
    fi

}

# Scripts for pre and post hibernate functions
do_install_append_batcam () {
   install -d ${D}${systemd_unitdir}/system-sleep/
   install -m 0755 ${WORKDIR}/pre_hibernate.sh -D ${D}${systemd_unitdir}/system-sleep/pre_hibernate.sh
   install -m 0755 ${WORKDIR}/post_hibernate.sh -D ${D}${systemd_unitdir}/system-sleep/post_hibernate.sh

}

SYSTEMD_SERVICE_${PN} += "${@bb.utils.contains('DISTRO_FEATURES','ab-boot-support',' set-slotsuffix.service','',d)}"
