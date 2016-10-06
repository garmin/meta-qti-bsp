inherit autotools-brokensep

DESCRIPTION = "Rebooter daemon"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=3775480a712fc46a69647678acb234cb"
PR = "r4"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://mdm-ss-mgr/reboot-daemon"

S = "${WORKDIR}/reboot-daemon"

EXTRA_OEMAKE_append = " CROSS=${HOST_PREFIX}"

do_install() {
    install -m 0755 ${S}/reboot-daemon -D ${D}/sbin/reboot-daemon
}
