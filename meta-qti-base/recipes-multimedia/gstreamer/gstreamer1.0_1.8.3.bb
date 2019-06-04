DEFAULT_PREFERENCE = "-1"

require gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer;nobranch=1;name=gstreamer"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/common;branch=gstreamer/common/master;name=common"

SRCREV_gstreamer = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/gstreamer/gstreamer', d)}"

DEPENDS = "gobject-introspection bison-native"

SRCREV_common = "6f2d2093e84cc0eb99b634fa281822ebb9507285"
S = "${WORKDIR}/gstreamer"

GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

INSANE_SKIP_${PN} += "installed-vs-shipped"
