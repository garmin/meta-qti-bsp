include wpa-supplicant.inc

PR = "${INC_PR}.1"

SRC_URI += "file://defconfig-ath6kl \
            "

DEPENDS += "qmi"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-ath6kl .config
}

