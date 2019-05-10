FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
DEPENDS = "base-passwd"

SRC_URI_append += "file://fstab"
SRC_URI_append += "file://${BASEMACHINE}/fstab"

dirs755_append = " /media/cf /media/net /media/ram \
            /media/union /media/realroot /media/hdd /media/mmc1"
dirs755_append +="/firmware /dsp /bluetooth /var /media/card /persist"

# userdata mount point is present by default in all machines.
# TODO: Add this path to MACHINE_MNT_POINTS in machine conf.
dirs755_append = " ${userfsdatadir}"

dirs755_append = " ${MACHINE_MNT_POINTS}"

# /systemrw partition is needed only when system is RO.
# Otherwise files can be directly written to / itself.
dirs755_append = " ${@bb.utils.contains('DISTRO_FEATURES','ro-rootfs','/systemrw','',d)}"

# Explicitly remove sepolicy entries from fstab when selinux is not present.
fix_sepolicies () {
    #For /run
    sed -i "s#,rootcontext=system_u:object_r:var_run_t:s0##g" ${WORKDIR}/fstab
    # For /var/volatile
    sed -i "s#,rootcontext=system_u:object_r:var_t:s0##g" ${WORKDIR}/fstab
}
do_install[prefuncs] += " ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '', 'fix_sepolicies', d)}"

do_install_append(){
    install -m 755 -o diag -g diag -d ${D}/media
    install -m 755 -o diag -g diag -d ${D}/mnt/sdcard

    ln -s /mnt/sdcard ${D}/sdcard

    rmdir ${D}/tmp
    ln -s /var/tmp ${D}/tmp

    if [ ${BASEMACHINE} == "mdm9650" ]; then
      ln -s /etc/resolvconf/run/resolv.conf ${D}/etc/resolv.conf
    else
      ln -s /var/run/resolv.conf ${D}/etc/resolv.conf
    fi

    install -m 0644 ${WORKDIR}/fstab ${D}${sysconfdir}/fstab

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d 0644 ${D}${sysconfdir}/systemd/system
        install -d 0644 ${D}${sysconfdir}/systemd/system/local-fs.target.requires
    fi
    
    # use fstab for mount management
    rm -rf ${D}${sysconfdir}/systemd/system
    rm -rf ${D}${systemd_unitdir}/system

    if [ -L ${D}/usr/local ]; then
        rm ${D}/usr/local
    fi

    install -m 755 -o diag -g diag -d ${D}/media/card
    rm -rf ${D}/sdcard
    ln -s /media/card ${D}/sdcard
}





do_install_append_smack () {
      # replace the old smack rules from "System _ -----l" to "System _ rwxa--"
      sed -i "1c System _ rwxa--"  ${D}/${sysconfdir}/smack/accesses.d/default-access-domains
}



