SUMMARY = "hibernation init file"
DESCRIPTION = "Prepare hibernation environment"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI  = "file://init.qti.hibernate.sh"

S = "${WORKDIR}"

inherit systemd

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -m 0755 ${S}/init.qti.hibernate.sh -D ${D}${sysconfdir}/initscripts/init_hibernate
}

FILES_${PN} += "${sysconfdir}/initscripts/init_hibernate"
