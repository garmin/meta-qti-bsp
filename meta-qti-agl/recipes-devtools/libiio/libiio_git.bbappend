do_install_append() {
    # make libiio support multilib
    if [ "$libdir" == "/usr/lib64" -a -d ${D}/usr/lib ]; then
        mv ${D}/usr/lib/* ${D}/${libdir}/
        rm -rf ${D}/usr/lib
    fi
}
