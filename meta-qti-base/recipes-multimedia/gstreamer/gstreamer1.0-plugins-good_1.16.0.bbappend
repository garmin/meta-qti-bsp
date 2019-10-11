DEFAULT_PREFERENCE = "-1"

SRC_URI_remove += " file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
            file://0001-scaletempo-Advertise-interleaved-layout-in-caps-temp.patch \
            file://headerfix.patch \
            "
SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gst-plugins-good/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-good;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-good/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "good_common"

S = "${WORKDIR}/gstreamer/gst-plugins-good"

PACKAGECONFIG[v4l2]       = "--enable-gst_v4l2 --disable-v4l2-probe,--disable-gst_v4l2"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

RPROVIDES_${PN}-souphttpsrc = "${PN}-soup"
