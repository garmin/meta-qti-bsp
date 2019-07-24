DESCRIPTION = "memory hotplug"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI += "file://memory-hotplug.sh"
SRC_URI +="file://memory-hotplug.service"

inherit systemd
SYSTEMD_SERVICE_${PN} = "memory-hotplug.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
    install -D -m 0755 ${WORKDIR}/memory-hotplug.sh ${D}${bindir}/memory-hotplug.sh
    install -D -m 0644 ${WORKDIR}/memory-hotplug.service ${D}${systemd_unitdir}/system/memory-hotplug.service
}

FILES_${PN} += "usr/bin/memory-hotplug.sh \
    ${systemd_unitdir}/system/memory-hotplug.service"
