FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
DEPENDS = "base-passwd"

SRC_URI_append += "file://fstab"
SRC_URI_append += "file://systemd/cache.mount"
SRC_URI_append += "file://systemd/data.mount"
SRC_URI_append += "file://systemd/firmware.mount"
SRC_URI_append += "file://systemd/firmware-mount.service"
SRC_URI_append += "file://systemd/systemrw.mount"
SRC_URI_append += "file://systemd/dsp.mount"
SRC_URI_append += "file://systemd/dsp-mount.service"
SRC_URI_append += "file://systemd/media-card.mount"
SRC_URI_append += "file://systemd/media-ram.mount"
SRC_URI_append += "file://systemd/persist.mount"
SRC_URI_append += "file://systemd/var-volatile.mount"
SRC_URI_append += "file://systemd/proc-bus-usb.mount"
SRC_URI_append += "file://systemd/dash.mount"
SRC_URI_append += "file://systemd/cache-ubi.mount"
SRC_URI_append += "file://systemd/persist-ubi.mount"
SRC_URI_append += "file://systemd/data-ubi.mount"
SRC_URI_append += "file://systemd/systemrw-ubi.mount"
SRC_URI_append += "file://systemd/firmware-ubi-mount.sh"
SRC_URI_append += "file://systemd/firmware-ubi-mount.service"
SRC_URI_append += "file://systemd/dsp-ubi-mount.sh"
SRC_URI_append += "file://systemd/dsp-ubi-mount.service"
SRC_URI_append += "file://systemd/bluetooth-ubi-mount.sh"
SRC_URI_append += "file://systemd/bluetooth-ubi-mount.service"
SRC_URI_append += "file://systemd/bluetooth.mount"
SRC_URI_append += "file://systemd/bluetooth-mount.service"
SRC_URI_append += "file://systemd/non-hlos-squash.sh"

dirs755_append = " /media/cf /media/net /media/ram \
            /media/union /media/realroot /media/hdd /media/mmc1"

# userdata mount point is present by default in all machines.
# TODO: Add this path to MACHINE_MNT_POINTS in machine conf.
dirs755_append = " ${userfsdatadir}"

dirs755_append = " ${MACHINE_MNT_POINTS}"

# /systemrw partition is needed only when system is RO.
# Otherwise files can be directly written to / itself.
dirs755_append = " ${@bb.utils.contains('DISTRO_FEATURES','ro-rootfs','/systemrw','',d)}"

# Various mount related files added here assume selinux support by default.
# Explicitly remove sepolicy entries when selinux is not present.
fix_sepolicies () {
    sed -i "s#,context=system_u:object_r:firmware_t:s0##g" ${WORKDIR}/systemd/firmware.mount
    sed -i "s#,context=system_u:object_r:firmware_t:s0##g" ${WORKDIR}/systemd/firmware-mount.service
    sed -i "s#,context=system_u:object_r:firmware_t:s0##g" ${WORKDIR}/systemd/bluetooth.mount
    sed -i "s#,context=system_u:object_r:firmware_t:s0##g" ${WORKDIR}/systemd/bluetooth-mount.service
    sed -i "s#,context=system_u:object_r:adsprpcd_t:s0##g" ${WORKDIR}/systemd/dsp-mount.service
    sed -i "s#,rootcontext=system_u:object_r:var_t:s0##g"  ${WORKDIR}/systemd/var-volatile.mount
    sed -i "s#,rootcontext=system_u:object_r:system_data_t:s0##g"  ${WORKDIR}/systemd/systemrw.mount
    sed -i "s#,rootcontext=system_u:object_r:data_t:s0##g"  ${WORKDIR}/systemd/data.mount

    # Remove selinux entries from fstab
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
}

do_install_append_msm() {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d 0644 ${D}${sysconfdir}/systemd/system
        install -d 0644 ${D}${sysconfdir}/systemd/system/local-fs.target.requires
        install -d 0644 ${D}${systemd_unitdir}/system
        install -d 0644 ${D}${systemd_unitdir}/system/local-fs.target.requires
        install -d 0644 ${D}${systemd_unitdir}/system/sysinit.target.wants

        for d in ${dirs755}; do
            if [ "$d" == "$userfsdatadir" ]; then
                if ${@bb.utils.contains('DISTRO_FEATURES', 'full-disk-encryption', 'false', 'true', d)}; then
                    if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                        install -m 0644 ${WORKDIR}/systemd/data.mount ${D}${systemd_unitdir}/system/data.mount

                        # Run fsck at boot
                        install -d 0644 ${D}${systemd_unitdir}/system/local-fs-pre.target.requires
                        ln -sf ${systemd_unitdir}/system/systemd-fsck@.service \
                           ${D}${systemd_unitdir}/system/local-fs-pre.target.requires/systemd-fsck@dev-disk-by\\x2dpartlabel-userdata.service
                    else
                        install -m 0644 ${WORKDIR}/systemd/data-ubi.mount ${D}${systemd_unitdir}/system/data.mount
                    fi
                    ln -sf  ${systemd_unitdir}/system/data.mount ${D}${systemd_unitdir}/system/sysinit.target.wants/data.mount
                fi
            fi

            if [ "$d" == "/cache" ]; then
                if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                    install -m 0644 ${WORKDIR}/systemd/cache.mount ${D}${systemd_unitdir}/system/cache.mount
                else
                    install -m 0644 ${WORKDIR}/systemd/cache-ubi.mount ${D}${systemd_unitdir}/system/cache.mount
                fi
                ln -sf  ${systemd_unitdir}/system/cache.mount ${D}${systemd_unitdir}/system/sysinit.target.wants/cache.mount
            fi

            if [ "$d" == "/persist" ]; then
                if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                    install -m 0644 ${WORKDIR}/systemd/persist.mount ${D}${systemd_unitdir}/system/persist.mount
                else
                    if ${@bb.utils.contains('DISTRO_FEATURES','persist-volume','true','false',d)}; then
                        install -m 0644 ${WORKDIR}/systemd/persist-ubi.mount ${D}${systemd_unitdir}/system/persist.mount
                    fi
                fi
                ln -sf  ${systemd_unitdir}/system/persist.mount ${D}${systemd_unitdir}/system/sysinit.target.wants/persist.mount
            fi

            # If the AB boot feature is enabled, then instead of <partition>.mount,
            # a <partition-mount>.service invokes mounting the A/B partition as detected at the time of boot.
            if ${@bb.utils.contains('DISTRO_FEATURES','ab-boot-support','true','false',d)}; then
                if [ "$d" == "/firmware" ]; then
                    install -m 0644 ${WORKDIR}/systemd/firmware-mount.service ${D}${sysconfdir}/systemd/system/firmware-mount.service
                    ln -sf  ../firmware-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/firmware-mount.service
                fi
                if [ "$d" == "/dsp" ]; then
                    install -m 0644 ${WORKDIR}/systemd/dsp-mount.service ${D}${sysconfdir}/systemd/system/dsp-mount.service
                    ln -sf  ../dsp-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/dsp-mount.service
                fi
                if [ "$d" == "/bt_firmware" ]; then
                    install -m 0644 ${WORKDIR}/systemd/bluetooth-mount.service ${D}${sysconfdir}/systemd/system/bluetooth-mount.service
                    ln -sf  ../bluetooth-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/bluetooth-mount.service
                fi
            # non-AB boot
            else
                if [ "$d" == "/firmware" ]; then
                    if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                        install -m 0644 ${WORKDIR}/systemd/firmware.mount ${D}${sysconfdir}/systemd/system/firmware.mount
                        ln -sf  ../firmware.mount  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/firmware.mount
                    else
                        install -d 0644 ${D}${sysconfdir}/initscripts
                        install -m 0644 ${WORKDIR}/systemd/firmware-ubi-mount.service ${D}${sysconfdir}/systemd/system/firmware-mount.service
                        if ${@bb.utils.contains('DISTRO_FEATURES','nand-squashfs','true','false',d)}; then
                            install -m 0744 ${WORKDIR}/systemd/non-hlos-squash.sh ${D}${sysconfdir}/initscripts/firmware-ubi-mount.sh
                        else
                            install -m 0744 ${WORKDIR}/systemd/firmware-ubi-mount.sh ${D}${sysconfdir}/initscripts/firmware-ubi-mount.sh
                        fi
                        ln -sf  ../firmware-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/firmware-mount.service
                    fi
                fi
                if [ "$d" == "/dsp" ]; then
                    if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                        install -m 0644 ${WORKDIR}/systemd/dsp.mount ${D}${sysconfdir}/systemd/system/dsp.mount
                        ln -sf  ../dsp.mount  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/dsp.mount
                    else
                        install -d 0644 ${D}${sysconfdir}/initscripts
                        install -m 0644 ${WORKDIR}/systemd/dsp-ubi-mount.service ${D}${sysconfdir}/systemd/system/dsp-mount.service
                        install -m 0744 ${WORKDIR}/systemd/dsp-ubi-mount.sh ${D}${sysconfdir}/initscripts/dsp-ubi-mount.sh
                        ln -sf  ../dsp-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/dsp-mount.service
                    fi
                fi
                if [ "$d" == "/bt_firmware" ]; then
                    if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                        install -m 0644 ${WORKDIR}/systemd/bluetooth.mount ${D}${sysconfdir}/systemd/system/bluetooth.mount
                        ln -sf  ../bluetooth.mount  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/bluetooth.mount
                    else
                        install -d 0644 ${D}${sysconfdir}/initscripts
                        install -m 0644 ${WORKDIR}/systemd/bluetooth-ubi-mount.service ${D}${sysconfdir}/systemd/system/bluetooth-mount.service
                        install -m 0744 ${WORKDIR}/systemd/bluetooth-ubi-mount.sh ${D}${sysconfdir}/initscripts/bluetooth-ubi-mount.sh
                        ln -sf  ../bluetooth-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/bluetooth-mount.service
                    fi
                fi
            fi
            # systemrw is applicable only when rootfs is read only.
            if ${@bb.utils.contains('DISTRO_FEATURES','ro-rootfs','true','false',d)}; then
                if [ "$d" == "/systemrw" ]; then
                    if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                        install -m 0644 ${WORKDIR}/systemd/systemrw.mount ${D}${systemd_unitdir}/system/systemrw.mount

                        # Run fsck at boot
                        install -d 0644 ${D}${systemd_unitdir}/system/local-fs-pre.target.requires
                        ln -sf ${systemd_unitdir}/system/systemd-fsck@.service \
                             ${D}${systemd_unitdir}/system/local-fs-pre.target.requires/systemd-fsck@dev-disk-by\\x2dpartlabel-systemrw.service
                    else
                        install -m 0644 ${WORKDIR}/systemd/systemrw-ubi.mount ${D}${systemd_unitdir}/system/systemrw.mount
                    fi
                    ln -sf  ${systemd_unitdir}/system/systemrw.mount ${D}${systemd_unitdir}/system/local-fs.target.requires/systemrw.mount
                fi
            fi
        done
    fi
}
