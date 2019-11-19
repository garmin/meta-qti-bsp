FILESEXTRAPATHS_append := ":${THISDIR}/weston/"
SRC_URI = "${PATH_TO_REPO}/graphics/weston/.git;protocol=${PROTO};destsuffix=graphics/weston;usehead=1"
S = "${WORKDIR}/graphics/weston"
SRCREV = "${AUTOREV}"

SRC_URI_append = "  \
    file://weston.service_caf \
    file://weston.ini_caf \
    file://drm_firmware_load_trigger.service \
"

#Remove community patch which is conflict with Weston SDM optimization
SRC_URI_remove = "file://0001-compositor-drm.c-Launch-without-input-devices.patch"

UPSTREAM_CHECK_URI_remove = "https://wayland.freedesktop.org/releases.html"

inherit systemd
REQUIRED_DISTRO_FEATURES_remove = "opengl"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "weston.service"

DEPENDS +="gbm-headers gbm"
DEPENDS += "libion libsync"
DEPENDS += "display-hal-linux display-noship-linux gbm-headers display-ship-linux display-hal-headers"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', 'libuhab', '', d)}"

RRECOMMENDS_${PN}_remove = "weston-conf"

TARGET_CFLAGS += "-idirafter ${STAGING_KERNEL_BUILDDIR}/include/"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/libdrm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm/core"
TARGET_CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/qcom/display"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"

EXTRA_OECONF += "--disable-xwayland \
                --enable-simple-clients \
                --enable-clients \
                --enable-demo-clients-install \
                --disable-wayland-compositor \
                --disable-fbdev-compositor \
                --enable-drm-compositor    \
                "
EXTRA_OECONF_append_qemux86 = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"
EXTRA_OECONF_append_qemux86-64 = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"

#Overwrite Packageconfig
PACKAGECONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'kms fbdev wayland egl', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'launch', '', d)} \
                  "
#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "--enable-drm-compositor,--disable-drm-compositor,drm udev virtual/egl mtdev"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,virtual/egl"
# weston-launch
PACKAGECONFIG[launch] = "--enable-weston-launch,--disable-weston-launch,libpam drm"
# Clients support
PACKAGECONFIG[clients] = " "

do_install_append() {
	# Install systemd unit files
		if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
		install -m 644 -p -D ${WORKDIR}/weston.service_caf ${D}${systemd_system_unitdir}/weston.service
	fi

	WESTON_INI_CONFIG=${sysconfdir}/xdg/weston
	install -d ${D}${WESTON_INI_CONFIG}
	install -m 0644 ${WORKDIR}/weston.ini_caf ${D}${WESTON_INI_CONFIG}/weston.ini
	# Install reuqire-input=false in weston.ini
	if ${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', 'true', 'false', d)}; then
	    sed -i -e '/\[core\]/a require-input=false' ${D}${WESTON_INI_CONFIG}/weston.ini
	fi
	# expose weston protocol to /usr/share/weston as video may use it
	install ${WORKDIR}/graphics/weston/protocol/*.xml ${D}${datadir}/weston
}

FILES_${PN}-staticdev += "${libdir}/*.a"
FILES_${PN} += "${libdir}/*"
FILES_${PN} += "${systemd_unitdir}/system/ ${sysconfdir}/"
