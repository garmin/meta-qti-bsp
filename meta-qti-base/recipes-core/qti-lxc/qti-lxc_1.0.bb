SUMMARY = "Set up lxc host enviroment"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI += "file://lxc-init.service"
SRC_URI += "file://lxc-start.service"
SRC_URI += " file://lxc-init.sh"
SRC_URI += " file://lxc-start.sh"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE_${PN} = "lxc-init.service"
SYSTEMD_SERVICE_${PN} += "lxc-start.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
    install -D -m 0755 ${WORKDIR}/lxc-init.sh ${D}${base_sbindir}/lxc-init.sh
    install -D -m 0755 ${WORKDIR}/lxc-start.sh ${D}${base_sbindir}/lxc-start.sh
    install -D -m 0644 ${WORKDIR}/lxc-init.service ${D}${systemd_unitdir}/system/lxc-init.service
    install -D -m 0644 ${WORKDIR}/lxc-start.service ${D}${systemd_unitdir}/system/lxc-start.service
    install -d ${D}/lxc
}

FILES_${PN} += "${systemd_unitdir}/system/lxc-init.service"
FILES_${PN} += "${systemd_unitdir}/system/lxc-start.service"
FILES_${PN} += "${sbindir}/lxc-init.sh"
FILES_${PN} += "${sbindir}/lxc-start.sh"
FILES_${PN} += "/lxc"
