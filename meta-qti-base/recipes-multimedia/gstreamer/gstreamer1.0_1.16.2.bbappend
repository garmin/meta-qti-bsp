DEFAULT_PREFERENCE = "-1"

FILESEXTRAPATHS_append := ":${THISDIR}/gstreamer"
SRC_URI += "file://0001-change-for-build-error.patch \
           "
DEPENDS = "gobject-introspection bison-native"

GI_DATA_ENABLED="0"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

