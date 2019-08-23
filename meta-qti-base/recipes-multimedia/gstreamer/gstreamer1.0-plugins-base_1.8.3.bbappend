DEFAULT_PREFERENCE = "-1"


SRC_URI_remove= "\
    file://0001-Makefile.am-don-t-hardcode-libtool-name-when-running.patch \
    file://0002-Makefile.am-prefix-calls-to-pkg-config-with-PKG_CONF.patch \
    file://0003-riff-add-missing-include-directories-when-calling-in.patch \
    file://0004-rtsp-drop-incorrect-reference-to-gstreamer-sdp-in-Ma.patch \
"
SRC_URI_remove += " file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch"
SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-base/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-base;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-base/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "6f2d2093e84cc0eb99b634fa281822ebb9507285"
SRCREV_FORMAT = "base_common"
S = "${WORKDIR}/gstreamer/gst-plugins-base"

GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
