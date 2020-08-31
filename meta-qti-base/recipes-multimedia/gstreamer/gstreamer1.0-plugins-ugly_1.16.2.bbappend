DEFAULT_PREFERENCE = "-1"

DEPENDS += "opencore-amr"

PACKAGECONFIG ??= "orc opencore-amr"

EXTRA_OEMESON += " \
    -Da52dec=disabled \
    -Dcdio=disabled \
    -Ddvdlpcmdec=disabled \
    -Ddvdread=disabled \
    -Ddvdsub=disabled \
    -Dlame=disabled \
    -Dmad=disabled \
    -Dmpeg2dec=disabled \
    -Dmpg123=disabled \
    -Drealmedia=disabled \
    -Dsidplay=disabled \
    -Dtwolame=disabled \
    -Dx264=disabled \
    -Dxingmux=disabled \
    -Ddependency-tracking=disabled \
    -DFEATURE=disabled \
    -Damrnb=enabled \
    -Damrwbdec=enabled \
    -Dasfdemux=enabled \
    "
EXTRA_OEMESON_remove = "\
    -Damrnb=disabled \
    -Damrwbdec=disabled \
    "

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

