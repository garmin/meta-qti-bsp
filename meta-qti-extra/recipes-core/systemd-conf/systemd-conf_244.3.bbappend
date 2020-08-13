FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

do_install_append () {
 echo "HandlePowerKey=ignore" >> ${D}${systemd_unitdir}/logind.conf.d/00-${PN}.conf
}
