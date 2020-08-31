inherit systemd
DESCRIPTION = "Open AVB"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"

SRC_URI   =  "${PATH_TO_REPO}/external/open-avb/.git;protocol=${PROTO};destsuffix=external/open-avb;usehead=1"
SRC_URI += " file://gptp-daemon.service"
SRC_URI += " file://gptp-daemon-tmpfilesd.conf"
SRCREV = "${AUTOREV}"
PR = "r0"
PV = "0.1"

DEPENDS += "alsa-lib libpcap pciutils cmake-native glib-2.0 gstreamer1.0 gstreamer1.0-plugins-base"

S = "${WORKDIR}/external/open-avb"

GPTP_AUTO_START_ENABLE= "YES"
EXTRA_OEMAKE += "${@bb.utils.contains("DISTRO_FEATURES", "systemd", " SYSTEMD_SUPPORT_INCLUDED=1", "SYSTEMD_SUPPORT_INCLUDED=0", d)}"
EXTRA_OEMAKE += "${@oe.utils.conditional('GPTP_AUTO_START_ENABLE', 'YES', 'GPTP_AUTO_START=1', 'GPTP_AUTO_START=0', d)}"
SYSTEMD_SERVICE_${PN} = "${@oe.utils.conditional('GPTP_AUTO_START_ENABLE', 'YES', 'gptp-daemon.service', '', d)}"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"
PACKAGE_ARCH = "${MACHINE_ARCH}"

TARGET_CC_ARCH += "${LDFLAGS}"

SECURITY_CFLAGS_remove_pn-open-avb = "-D_FORTIFY_SOURCE=2"

do_compile_prepend() {
}

do_compile() {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', 'true', 'false', d)}; then
		export AVB_FEATURE_GVM_MODE=1
	fi
	export AVB_FEATURE_NEUTRINO=1
	export AVB_FEATURE_INTF_ALSA2=0
	export AVB_FEATURE_GSTREAMER=1
	export GSTREAMER_1_0=1
	echo ${FILESEXTRAPATHS}
	echo ${subdir}

	mkdir -p ${S}/daemons/maap/build
	oe_runmake daemons_all
	make avtp_pipeline
	oe_runmake libgptp
	oe_runmake libgptp_test
}

do_install() {
	mkdir -p ${D}/${bindirr}/
	mkdir -p ${D}/${bindir}/avb/
	install ${S}/daemons/maap/linux/maap_daemon ${D}/${bindir}/avb
	install ${S}/daemons/mrpd/mrpd ${D}/${bindir}/avb
	install ${S}/daemons/mrpd/mrpctl ${D}/${bindir}/avb
	install ${S}/daemons/gptp/linux/build/obj/daemon_cl ${D}/${bindir}/avb
	install ${S}/lib/avtp_pipeline/build/bin/* ${D}/${bindir}/avb
	mkdir -p ${D}/${libdir}/
	install ${S}/lib/avtp_pipeline/build/lib/*.so ${D}/${libdir}
	install ${S}/examples/libgptp_test/libgptp_test ${D}/${bindir}/avb
	install ${S}/lib/libgptp/*.so ${D}/${libdir}

	mkdir -p ${D}/${includedir}/
	install ${S}/lib/libgptp/gptp_helper.h ${D}${includedir}

	if (test "x${GPTP_AUTO_START_ENABLE}" == "xYES"); then
		if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
			install -d ${D}${systemd_unitdir}/system/
			install -m 0644 ${WORKDIR}/gptp-daemon.service -D ${D}${systemd_unitdir}/system/gptp-daemon.service
			install -d ${D}${sysconfdir}/tmpfiles.d/
			install -m 0644 ${WORKDIR}/gptp-daemon-tmpfilesd.conf ${D}${sysconfdir}/tmpfiles.d/gptp-daemon-tmpfilesd.conf
		fi
	fi
}

FILES_${PN} =+ "${bindir}/avb/*"
FILES_${PN} =+ "${libdir}/*"
FILES_${PN}-dev = "${includedir}/*"
FILES_${PN}-dbg += "${bindir}/avb/.debug/*"

INHIBIT_PACKAGE_STRIP="1"
INHIBIT_PACKAGE_DEBUG_SPLIT="1"

