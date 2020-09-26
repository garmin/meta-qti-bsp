FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://kdump-qti "

do_install_append() {
    echo "KDUMP_CMDLINE=\"clk_ignore_unused rcupdate.rcu_expedited=1 rcu_nocbs=0-7 root=/dev/ram rw rootwait console=ttyMSM0,115200,n8 lpm_levels.sleep_disabled=1 nokaslr 1 reset_devices minidump=1 androidboot.slot_suffix=_a\"" >> ${D}${sysconfdir}/sysconfig/kdump.conf
    echo "KDUMP_KIMAGE=\"/boot/Image\"" >> ${D}${sysconfdir}/sysconfig/kdump.conf
    echo "KDUMP_VMCORE_PATH=\"/data/crash/\`date +"%Y-%m-%d"\`\"" >> ${D}${sysconfdir}/sysconfig/kdump.conf
    sed -i "s/^MAKEDUMPFILE_ARGS.*$/MAKEDUMPFILE_ARGS=\"-d 31 -c\"/g" ${D}${sysconfdir}/sysconfig/kdump.conf
    sed -i "s/^MAKEDUMPFILE_ARGS.*$/MAKEDUMPFILE_ARGS=\"-d 31 -c\"/g" ${D}${libexecdir}/kdump-helper
    sed -i "s/--append/--command-line/g" ${D}${libexecdir}/kdump-helper
    sed -i "/do_save_vmcore$/i \\\t\techo 1 > \/proc\/sys\/kernel\/kptr_restrict" ${D}${libexecdir}/kdump-helper
    sed -i "s/^After=sysinit.target/After=sysinit.target data.mount/g" ${D}/lib/systemd/system/kdump.service
    sed -i "s/^WantedBy=multi-user.target/WantedBy=sysinit.target/g" ${D}/lib/systemd/system/kdump.service
    sed -i "s/reboot/sync \&\& echo c > \/proc\/sysrq-trigger/g" ${D}${libexecdir}/kdump-helper
    sed -i "s/kdump-helper/kdump-qti-helper/g" ${D}/lib/systemd/system/kdump.service

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -D -m 0755 ${WORKDIR}/kdump-qti ${D}${libexecdir}/kdump-qti-helper
    fi
}
