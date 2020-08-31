DEFAULT_PREFERENCE = "-1"

FILESEXTRAPATHS_append := ":${THISDIR}/gst-plugins-good"
SRC_URI += "file://0001-ignore-av_bsp-segment-error-in-wav-header.patch \
            file://0002-qtdemux-add-handle-of-shorter-stream.patch \
            "

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'pulseaudio x11', d)} \
    bz2 cairo flac gdk-pixbuf gudev jpeg-turbo lame libpng mpg123 soup speex taglib v4l2 \
"
PACKAGECONFIG[v4l2]       = "-Dgst_v4l2=enabled -Dv4l2-probe=false,-Dgst_v4l2=false"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

RPROVIDES_${PN}-souphttpsrc = "${PN}-soup"
