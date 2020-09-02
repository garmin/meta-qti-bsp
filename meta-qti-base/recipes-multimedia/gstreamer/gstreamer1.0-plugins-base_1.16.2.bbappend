DEFAULT_PREFERENCE = "-1"

FILESEXTRAPATHS_append := ":${THISDIR}/gst-plugins-base"
SRC_URI += "file://0001-gst-plugins-base-add-NV12_UBWC-and-RGBA_UBWC.patch \
            file://0002-Support-typefind-of-heic.patch \
            file://0003-Support-NV12_512-color-format-in-Gstreamer.patch \
            file://0004-videodecoder-expose-function-push_event.patch \
            file://0005-change-for-build-error.patch \
            file://0006-do-not-create-eglsink.patch \
            "

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    ${PACKAGECONFIG_GL} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'alsa x11', d)} \
    jpeg-turbo ogg pango png theora vorbis \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland egl', '', d)} \
"

DEPENDS += "libcutils"
GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
