do_install_append() {
	rm -f ${D}/${libdir}/libwayland-egl.so*
	rm -f ${D}/${libdir}/pkgconfig/wayland-egl.pc
}
