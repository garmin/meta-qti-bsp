DESCRIPTION = "Device specific config"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r0"


# Provide a baseline
SRC_URI = "${PATH_TO_REPO}/device/qcom/wlan/.git;protocol=${PROTO};destsuffix=device/qcom/wlan;nobranch=1"
# Update for each machine
S = "${WORKDIR}/device"
SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/device/qcom/wlan', d)}"

do_install_append_auto(){
	install -d ${D}/etc/misc/wifi
	install -m 0644 ${S}/qcom/wlan/sdx_auto/*.conf ${D}/etc/misc/wifi
	install -d ${D}/usr/bin
	install -m 0755 ${S}/qcom/wlan/sdx_auto/*.sh ${D}/usr/bin
}

do_install_append_automotive(){
	install -d ${D}/etc/misc/wifi
	install -m 0644 ${S}/qcom/wlan/msm_auto/*.conf ${D}/etc/misc/wifi
	install -d ${D}/usr/bin
	install -m 0755 ${S}/qcom/wlan/msm_auto/*.sh ${D}/usr/bin
}
