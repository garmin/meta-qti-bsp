do_install_append() {
        rm ${D}${systemd_unitdir}/network/80-wired.network
}

