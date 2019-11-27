DEFAULT_PREFERENCE = "-1"


SRC_URI_remove= "\
    file://0001-Makefile.am-don-t-hardcode-libtool-name-when-running.patch \
    file://0002-Makefile.am-prefix-calls-to-pkg-config-with-PKG_CONF.patch \
    file://0003-riff-add-missing-include-directories-when-calling-in.patch \
    file://0004-rtsp-drop-incorrect-reference-to-gstreamer-sdp-in-Ma.patch \
    file://0003-ssaparse-enhance-SSA-text-lines-parsing.patch \
"
SRC_URI_remove += " file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch"
SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-base/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-base;usehead=1 \
           https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/gstreamer/gstreamer1.0-plugins-base/0003-ssaparse-enhance-SSA-text-lines-parsing.patch?h=yocto/master;downloadfilename=0003-ssaparse-enhance-SSA-text-lines-parsing.patch;md5sum=e4622746e5f36d00e59447374981c5ea;sha256sum=603a7e57cf72c5dc73e227859fab9d7c17048efb4ede779494f9b370db1700c3 \
          "
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-base/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "base_common"
DEPENDS += "media"
DEPENDS += "libion"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CPPFLAGS += "-I${STAGING_INCDIR}/ion_headers"
CPPFLAGS += "-I${STAGING_INCDIR}/mm-core/"


S = "${WORKDIR}/gstreamer/gst-plugins-base"

GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
