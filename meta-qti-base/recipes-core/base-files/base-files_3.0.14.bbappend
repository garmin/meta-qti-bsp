FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
DEPENDS = "base-passwd"

SRC_URI_append += "file://${BASEMACHINE}/fstab"

dirs755_append = " /media/cf /media/net /media/ram \
            /media/union /media/realroot /media/hdd /media/mmc1"
dirs755_append +="/firmware /dsp /bluetooth /var /media/card /persist"

# userdata mount point is present by default in all machines.
# TODO: Add this path to MACHINE_MNT_POINTS in machine conf.
dirs755_append = " ${userfsdatadir}"

dirs755_append = " ${MACHINE_MNT_POINTS}"

# Explicitly remove sepolicy entries from fstab when selinux is not present.
fix_sepolicies () {
    #For /run
    sed -i "s#,rootcontext=system_u:object_r:var_run_t:s0##g" ${WORKDIR}/fstab
    # For /var/volatile
    sed -i "s#,rootcontext=system_u:object_r:var_t:s0##g" ${WORKDIR}/fstab
}
do_install[prefuncs] += " ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '', 'fix_sepolicies', d)}"

# Replace persist/home bind if read-only is not enabled
fix_read_only () {
    sed -i "/^\PARTLABEL=persist.*var/d" ${WORKDIR}/fstab
    sed -i "/^\#.*Bind/d" ${WORKDIR}/fstab
    sed -i "/^\/data/d" ${WORKDIR}/fstab
    sed -i "/^\/var/d" ${WORKDIR}/fstab
}

do_install[prefuncs] += " ${@bb.utils.contains('IMAGE_FEATURES', 'read-only-rootfs', '', 'fix_read_only', d)}"

do_install_append(){
    install -m 755 -o diag -g diag -d ${D}/media
    install -m 755 -o diag -g diag -d ${D}/media/card
    ln -s /media/card ${D}/sdcard
    rmdir ${D}/tmp
    ln -s /var/tmp ${D}/tmp
    ln -s /var/run/resolv.conf ${D}/etc/resolv.conf
    ln -s /lib ${D}/lib64
    ln -s /usr/lib ${D}/usr/lib64

    install -m 0644 ${WORKDIR}/fstab ${D}${sysconfdir}/fstab

}
