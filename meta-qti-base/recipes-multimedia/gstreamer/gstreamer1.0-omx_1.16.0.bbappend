DEFAULT_PREFERENCE = "-1"


DEPENDS += "media"
DEPENDS += "libion"
RDEPENDS_${PN} = "media"
GSTREAMER_1_0_OMX_TARGET = "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"


SRC_URI =  "${PATH_TO_REPO}/gstreamer/gst-omx/.git;protocol=${PROTO};destsuffix=gstreamer/gst-omx;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-omx/common;branch=gstreamer/common/master;name=common"
SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "omx_common"
S = "${WORKDIR}/gstreamer/gst-omx"

EXTRA_OECONF = " \
               --with-omx-target=qti \
               --with-omx-header-path=${STAGING_INCDIR}/mm-core \
               --with-protocal-xml-path=${STAGING_DATADIR}/weston \
              "
EXTRA_OECONF_append =" --enable-target-vpu554='yes'"
EXTRA_OECONF_append =" --enable-encoder-heic='yes'"

CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-I${STAGING_INCDIR}/ion_headers"

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
