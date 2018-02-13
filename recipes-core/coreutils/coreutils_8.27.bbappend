do_install_append_sdxpoorwills(){
	mv ${D}${base_bindir}/cp.coreutils ${D}/cp.coreutils;
	rm -rf ${D}${base_bindir};
	rm -rf ${D}/usr;
	install -d ${D}${base_bindir};
	mv ${D}/cp.coreutils ${D}${base_bindir}/cp.coreutils;
}

