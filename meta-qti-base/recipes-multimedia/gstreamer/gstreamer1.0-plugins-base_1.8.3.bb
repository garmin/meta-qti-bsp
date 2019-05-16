DEFAULT_PREFERENCE = "-1"

require gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=c54ce9345727175ff66d17b67ff51f58 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                   "

FILESPATH =+ "${WORKSPACE}/gstreamer:"
SRC_URI = "file://gst-plugins-base"
SRC_URI += "${CAF_GIT}/gstreamer/common;destsuffix=gst-plugins-base/common;branch=gstreamer/common/master;name=common"

SRCREV_common = "6f2d2093e84cc0eb99b634fa281822ebb9507285"
S = "${WORKDIR}/gst-plugins-base"

GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
