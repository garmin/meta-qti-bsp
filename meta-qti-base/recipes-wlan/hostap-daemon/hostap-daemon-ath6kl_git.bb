include hostap-daemon.inc

PR = "${INC_PR}.3"

SRC_URI += "file://defconfig-ath6kl"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-ath6kl .config
}

