inherit autotools pkgconfig useradd distro_features_check

DESCRIPTION = "Android Binder support"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r0"

DEPENDS = "liblog libcutils libhardware libselinux system-core glib-2.0"

SRC_URI  = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks/binder;subpath=binder;usehead=1"
SRC_URI_append = " file://servicemanager.service"
SRC_URI_append = " file://create-binder.sh"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/frameworks/binder"

EXTRA_OECONF += " --with-glib"

# This recipe assumes kernel always compile for default arch even when
# multilib compilation is enabled. If kernel is 64bit and binder is compiled
# for 32bit due to multilib settings default 64bit IPC need to be supported
# as kernel is 64bit. Only when kernel is 32bit, 32bit IPC need to be enabled.
EXTRA_OECONF_append_arm = " \
    ${@bb.utils.contains('MULTILIB_VARIANTS', 'lib32','','--enable-32bit-binder-ipc',d)} \
"

# sdmsteppe uses 64bit IPC though userspace is 32bit.
EXTRA_OECONF_remove_sdmsteppe = "--enable-32bit-binder-ipc"

CFLAGS += "-I${STAGING_INCDIR}/libselinux"

FILES_${PN}-dbg    = "${libdir}/.debug/libbinder.* ${bindir}/.debug/servicemanager ${bindir}/test_binder"
FILES_${PN}        = "${libdir}/libbinder.so.* ${libdir}/pkgconfig/* ${bindir}/servicemanager ${sysconfdir}/initscripts/* "
FILES_${PN}-dev    = "${libdir}/libbinder.so ${libdir}/libbinder.la ${includedir}"
FILES_${PN}-static = "${libdir}/libbinder.a"

QPERM_SERVICE = "${WORKDIR}/servicemanager.service"

do_install_append() {
   if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
       install -d ${D}/etc/initscripts/
       install -m 0755 ${WORKDIR}/create-binder.sh -D ${D}${sysconfdir}/initscripts/create-binder
       install -d ${D}${systemd_unitdir}/system/
       install -m 0644 ${WORKDIR}/servicemanager.service -D ${D}${systemd_unitdir}/system/servicemanager.service
       install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
       # enable the service for multi-user.target
       ln -sf ${systemd_unitdir}/system/servicemanager.service \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/servicemanager.service
   fi
}

FILES_${PN} += "${systemd_unitdir}/system/"

REQUIRED_DISTRO_FEATURES = "systemd"
