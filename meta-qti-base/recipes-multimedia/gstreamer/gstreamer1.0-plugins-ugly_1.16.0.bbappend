DEFAULT_PREFERENCE = "-1"

SRC_URI_remove += " file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch"
SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-ugly/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-ugly;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-ugly/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "ugly_common"

S = "${WORKDIR}/gstreamer/gst-plugins-ugly"
DEPENDS += "opencore-amr"

PACKAGECONFIG ??= "orc opencore-amr"

EXTRA_OECONF += " \
    --disable-a52dec \
    --disable-cdio \
    --disable-dvdlpcmdec \
    --disable-dvdread \
    --disable-dvdsub \
    --disable-lame \
    --disable-mad \
    --disable-mpeg2dec \
    --disable-mpg123 \
    --disable-realmedia \
    --disable-sidplay \
    --disable-twolame \
    --disable-x264 \
    --disable-xingmux \
    --disable-dependency-tracking \
    --disable-FEATURE \
    --enable-amrnb \
    --enable-amrwb \
    --enable-asfdemux \
    "
EXTRA_OECONF_remove = "\
    --disable-amrnb \
    --disable-amrwb \
    "

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

