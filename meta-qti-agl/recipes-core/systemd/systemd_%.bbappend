do_install_prepend() {
    install -d ${D}${base_libdir}/systemd/network/
    if [ "$base_libdir" != "lib" -a -d ${D}/lib ]; then
        install -m 0644 ${WORKDIR}/canbus-can.network ${D}${nonarch_base_libdir}/systemd/network/60-canbus-can.network
    fi
}

FILES_${PN} += "${base_libdir}/*"

