#systemd_units_root = "${@bb.utils.contains("IMAGE_FEATURES", "read-only-rootfs",'/home/root/.config/systemd', '/var/local/lib/systemd', d)}"

do_install_append_class-target() {
    # Fix afm-system-setup.service reset issue
    sed -i -e '/^Type/a\RemainAfterExit=yes' ${D}${systemd_system_unitdir}/afm-system-setup.service
    sed -i -e 's/mkdir/mkdir -p/' ${D}${systemd_system_unitdir}/afm-system-setup.service
    sed -i -e '/^PAMName/a\ExecStartPre=/bin/systemctl  daemon-reload' ${D}${systemd_system_unitdir}/afm-user-session@.service
    sed -i -e '/^ExecStartPre/a\ExecStartPre=/etc/init.d/smack reload' ${D}${systemd_system_unitdir}/afm-user-session@.service
}


pkg_postinst_${PN} () {
   # Fail on error.
    set -e

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        chgrp ${afm_name} $D${systemd_units_root}/system
        chgrp ${afm_name} $D${systemd_units_root}/system/afm-user-session@.target.wants
        chgrp ${afm_name} $D${systemd_units_root}/user/default.target.wants
        chgrp ${afm_name} $D${systemd_units_root}/user/sockets.target.wants
    fi
    chown ${afm_name}:${afm_name} $D${afm_datadir}
    chown ${afm_name}:${afm_name} $D${afm_datadir}/applications
    chown ${afm_name}:${afm_name} $D${afm_datadir}/icons

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/system
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/system/afm-user-session@.target.wants
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/user/default.target.wants
        chsmack -a 'System::Shared' -t $D${systemd_units_root}/user/sockets.target.wants
    fi
    chsmack -a 'System::Shared' -t $D${afm_datadir}
    chsmack -a 'System::Shared' -t $D${afm_datadir}/applications
    chsmack -a 'System::Shared' -t $D${afm_datadir}/icons

    exit 0
}

pkg_postinst_ontarget_${PN} () {
}
