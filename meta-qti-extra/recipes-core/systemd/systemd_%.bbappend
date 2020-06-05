FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://0032-systemd-add-bootkpi-marker-for-user-session.patch \
             ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'file://0034-systemd-add-handover-support-for-early-service.patch', '', d)} \
             file://power-switch.rules \
             file://usb_sleep.sh \
             file://bt_sleep.sh "

do_install_append() {
   install -d ${D}/${base_libdir}/systemd/system-sleep
   install -m 0755 ${WORKDIR}/usb_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/usb_sleep.sh
   install -m 0755 ${WORKDIR}/bt_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/bt_sleep.sh
}
