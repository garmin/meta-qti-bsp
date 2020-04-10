FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://0032-systemd-add-bootkpi-marker-for-user-session.patch \
			file://power-switch.rules "
