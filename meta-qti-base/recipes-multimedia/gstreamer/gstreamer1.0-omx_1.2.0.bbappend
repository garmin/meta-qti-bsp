DEFAULT_PREFERENCE = "-1"


DEPENDS += "media"
RDEPENDS_${PN} = "media"
GSTREAMER_1_0_OMX_TARGET = "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"


SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-omx/.git;protocol=${PROTO};destsuffix=gstreamer/gst-omx;nobranch=1;name=omx"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-omx/common;branch=gstreamer/common/master;name=common"
SRCREV_omx = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/gstreamer/gst-omx', d)}"
SRCREV_common = "1f5d3c3163cc3399251827235355087c2affa790"
S = "${WORKDIR}/gstreamer/gst-omx"

EXTRA_OECONF = " \
               --with-omx-target=qti \
               --with-omx-header-path=${STAGING_INCDIR}/mm-core \
               --with-protocal-xml-path=${STAGING_DATADIR}/weston \
              "
EXTRA_OECONF_append =" --enable-target-vpu554='yes'"

CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

delete_pkg_m4_file() {
    # Delete m4 files which we provide patched versions of but will be ignored
    # if these exist
	rm -f "${S}/common/m4/pkg.m4"
	rm -f "${S}/common/m4/gtk-doc.m4"
}
do_configure[prefuncs] += "delete_pkg_m4_file"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
