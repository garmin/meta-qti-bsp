inherit autotools pkgconfig

DESCRIPTION = "Android Multimedia Framework"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

PR = "r1"

DEPENDS += "libhardware system-media"
DEPENDS += "binder"
DEPENDS += "liblog"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://frameworks/av/"
SRC_URI += "file://automtp.sh"

S = "${WORKDIR}/frameworks/av"

EXTRA_OECONF += " --with-kernel-headers=${STAGING_KERNEL_DIR}/include/uapi"

FILES_${PN}-dbg    = "${libdir}/.debug/libcamera_client.* ${bindir}/.debug/*"
FILES_${PN}        = "${libdir}/libcamera_client.so.* ${libdir}/pkgconfig/* ${bindir}/mtpserver ${sysconfdir}/udev/scripts/automtp.sh"
FILES_${PN}-dev    = "${libdir}/libcamera_client.so ${libdir}/libcamera_client.la ${includedir}"

CPPFLAGS += "-I${STAGING_INCDIR}/camera"
LDFLAGS += "-llog"

do_install_append() {
   if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
       install -d ${D}${sysconfdir}/udev/scripts/
       install -m 0755 ${WORKDIR}/automtp.sh \
             ${D}${sysconfdir}/udev/scripts/automtp.sh
   fi
}
