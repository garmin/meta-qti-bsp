SUMMARY = "Scripts for setup LV GVM Network"
SECTION = "network"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

inherit systemd

FILESEXTRAPATHS_append := ":${THISDIR}/setup-network"
SRC_URI = " file://setup-network.sh \
            file://setup-network.service \
          "

RDEPENDS_${PN} += "bash"

do_install() {
  install -d ${D}${systemd_system_unitdir}
  install -d ${D}${bindir}
  install -m 0755 ${WORKDIR}/setup-network.sh ${D}${bindir}/setup-network.sh
  install -m 0644 ${WORKDIR}/setup-network.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE_${PN} = "setup-network.service"
