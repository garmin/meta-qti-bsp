FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI += "file://cynara_update_policy.service"

# Force cynara/security-manager policy generated at first boot
pkg_postinst_${PN} () {
    # Fail on error.
    set -e
    mkdir -p $D${sysconfdir}/cynara
    ${CHSMACK} -a System $D${sysconfdir}/cynara

    exit 0
}

pkg_postinst_ontarget_${PN} () {
}

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
    install -d ${D}${systemd_unitdir}/system/sysinit.target.wants/
    install -d ${D}/var/lib
    touch ${D}/var/lib/need_update_cynara_policy
    install ${WORKDIR}/cynara_update_policy.service ${D}${systemd_unitdir}/system/cynara_update_policy.service
    ln -sf ${systemd_unitdir}/system/cynara.service ${D}${systemd_unitdir}/system/multi-user.target.wants/cynara.service
    ln -sf ${systemd_unitdir}/system/cynara_update_policy.service ${D}${systemd_unitdir}/system/sysinit.target.wants/cynara_update_policy.service
}
