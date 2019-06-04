DEFAULT_PREFERENCE = "-1"

require gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gst-plugins-good/.git;protocol=${PROTO};destsuffix=gst-plugins-good;nobranch=1;name=good"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gst-plugins-good/common;branch=gstreamer/common/master;name=common"

SRCREV_good = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/gstreamer/gst-plugins-good', d)}"
SRCREV_common = "6f2d2093e84cc0eb99b634fa281822ebb9507285"
S = "${WORKDIR}/gst-plugins-good"

PACKAGECONFIG[v4l2]       = "--enable-gst_v4l2 --disable-v4l2-probe,--disable-gst_v4l2"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

RPROVIDES_${PN}-souphttpsrc = "${PN}-soup"
