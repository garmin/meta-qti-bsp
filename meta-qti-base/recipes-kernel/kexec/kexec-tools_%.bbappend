
do_install_append() {
    echo "KDUMP_CMDLINE=\"rcupdate.rcu_expedited=1 rcu_nocbs=0-7 root=/dev/ram rw rootwait console=ttyMSM0,115200,n8 lpm_levels.sleep_disabled=1 nokaslr 1 reset_devices androidboot.slot_suffix=_a\"" >> ${D}${sysconfdir}/sysconfig/kdump.conf
    echo "KDUMP_KIMAGE=\"/boot/Image\"" >> ${D}${sysconfdir}/sysconfig/kdump.conf
    echo "KDUMP_VMCORE_PATH=\"/data/crash/\`date +"%Y-%m-%d"\`\"" >> ${D}${sysconfdir}/sysconfig/kdump.conf
    sed -i "s/^MAKEDUMPFILE_ARGS.*$/MAKEDUMPFILE_ARGS=\"-d 31 -c\"/g" ${D}${sysconfdir}/sysconfig/kdump.conf
    sed -i "s/^MAKEDUMPFILE_ARGS.*$/MAKEDUMPFILE_ARGS=\"-d 31 -c\"/g" ${D}${libexecdir}/kdump-helper
    sed -i "s/^KEXEC_ARGS.*$/KEXEC_ARGS=\"--initrd=\/boot\/machine-kdump-image-sa8155.cpio.gz  --dtb=\/boot\/sa8155p-v2-adp-air-capture.dtb -p\"/g" ${D}${libexecdir}/kdump-helper
    sed -i "s/--append/--command-line/g" ${D}${libexecdir}/kdump-helper
    sed -i "/do_save_vmcore$/i \\\t\techo 1 > \/proc\/sys\/kernel\/kptr_restrict" ${D}${libexecdir}/kdump-helper
}

