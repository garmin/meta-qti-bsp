include dbus.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://extra-users.conf"

INITSCRIPT_NAME = "dbus-1"
INITSCRIPT_PARAMS = "start 98 5 3 2 . stop 02 0 1 6 ."

do_install_append() {
   install -d ${D}/${datadir}/dbus-1/system.d/
   install -m 0644 ${WORKDIR}/extra-users.conf -D ${D}${datadir}/dbus-1/system.d/extra-users.conf
}


