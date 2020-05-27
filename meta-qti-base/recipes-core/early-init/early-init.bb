SUMMARY = "Early_init bash script for early_init feature"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI  = "file://early_init.c"
SRC_URI += "file://early_init.conf"
SRC_URI += "file://early_init_eth.conf"
SRC_URI += "file://early_init_noapp.conf"

SECURITY_CFLAGS_pn-${PN} = "${SECURITY_NOPIE_CFLAGS}"

S = "${WORKDIR}"

inherit systemd

do_compile() {
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
            ${CC} ${CFLAGS} ${LDFLAGS} -static -D EARLY_ETHERNET -D WESTON_USER=${WESTONUSER} -o ${S}/early_init ${S}/early_init.c
        else
            ${CC} ${CFLAGS} ${LDFLAGS} -static -o ${S}/early_init ${S}/early_init.c
        fi
}

do_install() {
    # Add early_init script for early_init feature
    install -d ${D}/run
    install -d ${D}/debug
    install -d ${D}${sbindir}
    install -m 0755 ${S}/early_init  ${D}${sbindir}/early_init
    install -d ${D}${sysconfdir}
    if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)} || ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
            install -m 0644 ${S}/early_init_eth.conf  ${D}${sysconfdir}/early_init.conf
        else
            if ${@bb.utils.contains('DISTRO_FEATURES', 'early_userspace', 'true', 'false', d)}; then
                install -m 0644 ${S}/early_init.conf  ${D}${sysconfdir}/early_init.conf
            else
                install -m 0644 ${S}/early_init_noapp.conf  ${D}${sysconfdir}/early_init.conf
            fi
        fi
    fi

    # if ${@bb.utils.contains('BASEMACHINE', '8x96autodvrs', 'true', 'false', d)}; then
    #     sed -i "s/-deinterlace=bob/-input=4/g" ${D}${sysconfdir}/early_init.conf
    # fi
    # sed -e 's,@XDG_RUNTIME_DIR@,${DISPLAY_XDG_RUNTIME_DIR},g' \
    #     -i ${D}${sysconfdir}/early_init.conf
}

FILES_${PN} += " ${sbindir}/early_init"
FILES_${PN} += " /run"
FILES_${PN} += " /debug"
