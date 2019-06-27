DEFAULT_PREFERENCE = "-1"

require gstreamer1.0-omx.inc

DEPENDS += "media"
RDEPENDS_${PN} = "media"
GSTREAMER_1_0_OMX_TARGET = "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://omx/gstomx.h;beginline=1;endline=21;md5=5c8e1fca32704488e76d2ba9ddfa935f"

SRC_URI =  "${PATH_TO_REPO}/gstreamer/gst-omx/.git;protocol=${PROTO};destsuffix=gstreamer/gst-omx;nobranch=1;name=omx"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-omx/common;branch=gstreamer/common/master;name=common"
SRCREV_omx = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/gstreamer/gst-omx', d)}"
SRCREV_common = "1f5d3c3163cc3399251827235355087c2affa790"
S = "${WORKDIR}/gstreamer/gst-omx"

EXTRA_OECONF = " \
               --with-omx-target=qti \
               --with-omx-header-path=${STAGING_INCDIR}/mm-core \
               --with-protocal-xml-path=${STAGING_DATADIR}/weston \
              "
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
