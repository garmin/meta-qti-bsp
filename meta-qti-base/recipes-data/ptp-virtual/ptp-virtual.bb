DESCRIPTION = "lib ptp"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/ptp-virtual/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/ptp-virtual;nobranch=1"
SRC_URI += " file://ptp-virtual.service"
SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/vendor/qcom/opensource/ptp-virtual', d)}"

DEPENDS = "virtual/kernel"

S = "${WORKDIR}/vendor/qcom/opensource/ptp-virtual"

inherit module module-sign kernel-arch qperf
INHIBIT_PACKAGE_STRIP = "1"
EXTRA_OEMAKE += "CONFIG_ARCH_MSM=y"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}/etc
    install -m 0644 ${WORKDIR}/ptp-virtual.service ${D}${systemd_unitdir}/system/ptp-virtual.service
}

FILES_${PN} += "${systemd_unitdir}/system/ptp-virtual.service \
                /etc/* \
" 
