DEFAULT_PREFERENCE = "-1"

SRC_URI_remove = " \
    file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
"
SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gstreamer/.git;protocol=${PROTO};destsuffix=gstreamer/gstreamer;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gstreamer/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "gstreamer_common"

DEPENDS = "gobject-introspection bison-native"

SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
S = "${WORKDIR}/gstreamer/gstreamer"
# qemu-mips64: error while loading shared libraries: .../recipe-sysroot/usr/lib/libgthread-2.0.so.0: ELF file data encoding not little-endian
GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

