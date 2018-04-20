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
SRC_URI_append += "file://systemd/ab_mount.sh"
SRC_URI_append += "file://systemd/cache-ubi.mount"
SRC_URI_append += "file://systemd/data-ubi.mount"
SRC_URI_append += "file://systemd/systemrw-ubi.mount"
SRC_URI_append += "file://systemd/firmware-ubi-mount.sh"
SRC_URI_append += "file://systemd/firmware-ubi-mount.service"

dirs755 += "/media/cf /media/net /media/ram \
            /media/union /media/realroot /media/hdd \
            /media/mmc1 /systemrw"

dirs755_append_apq8053 +="/firmware /persist /cache /dsp "
dirs755_append_apq8009 += "/firmware /persist /cache"
dirs755_append_apq8017 += "/firmware /persist /cache /dsp"
dirs755_append_qcs605 += "/firmware /persist /cache /dsp"
dirs755_append_qcs405-som1 += "/firmware /persist"
dirs755_append_qcs403-som2 += "/firmware /cache"
# Remove sepolicy entries from various files when selinux is not present.
do_fix_sepolicies () {
    if ${@bb.utils.contains('DISTRO_FEATURES','selinux','false','true',d)}; then
        # For mount services
        sed -i "s#,context=system_u:object_r:firmware_t:s0##g" ${WORKDIR}/systemd/firmware.mount
        sed -i "s#,context=system_u:object_r:firmware_t:s0##g" ${WORKDIR}/systemd/firmware-mount.service
        sed -i "s#,rootcontext=system_u:object_r:var_t:s0##g"  ${WORKDIR}/systemd/var-volatile.mount
        sed -i "s#,rootcontext=system_u:object_r:system_data_t:s0##g"  ${WORKDIR}/systemd/systemrw.mount
        # Remove selinux entries from fstab
        #For /run
        sed -i "s#,rootcontext=system_u:object_r:var_run_t:s0##g" ${WORKDIR}/fstab
        # For /var/volatile
        sed -i "s#,rootcontext=system_u:object_r:var_t:s0##g" ${WORKDIR}/fstab
    fi
}

addtask fix_sepolicies before do_install after do_compile

do_install_basefilesissue[vardepsexclude] += "DISTRO_NAME"
do_install_basefilesissue[vardepsexclude] += "DISTRO_VERSION"

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
        # userdata is present by default.
        if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
        install -m 0644 ${WORKDIR}/systemd/data.mount ${D}${sysconfdir}/systemd/system/data.mount
        else
        install -m 0644 ${WORKDIR}/systemd/data-ubi.mount ${D}${sysconfdir}/systemd/system/data.mount
        fi
        ln -sf  ../data.mount  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/data.mount
        for d in ${dirs755}; do
            if [ "$d" == "/cache" ]; then
                if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                install -m 0644 ${WORKDIR}/systemd/cache.mount ${D}${sysconfdir}/systemd/system/cache.mount
                else
                install -m 0644 ${WORKDIR}/systemd/cache-ubi.mount ${D}${sysconfdir}/systemd/system/cache.mount
                fi
                ln -sf  ../cache.mount  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/cache.mount
            fi
            if [ "$d" == "/persist" ]; then
                install -m 0644 ${WORKDIR}/systemd/persist.mount ${D}${sysconfdir}/systemd/system/persist.mount
                ln -sf  ../persist.mount  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/persist.mount
            fi

            # If the AB boot feature is enabled, then instead of <partition>.mount,
            # a <partition-mount>.service invokes mounting the A/B partition as detected at the time of boot.
            if ${@bb.utils.contains('DISTRO_FEATURES','ab-boot-support','true','false',d)};then
                install -d 0644 ${D}${sysconfdir}/initscripts
                install -m 0744 ${WORKDIR}/systemd/ab_mount.sh ${D}${sysconfdir}/initscripts/ab_mount.sh
                if [ "$d" == "/firmware" ]; then
                    install -m 0644 ${WORKDIR}/systemd/firmware-mount.service ${D}${sysconfdir}/systemd/system/firmware-mount.service
                    ln -sf  ../firmware-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/firmware-mount.service
                fi
                if [ "$d" == "/dsp" ]; then
                    install -m 0644 ${WORKDIR}/systemd/dsp-mount.service ${D}${sysconfdir}/systemd/system/dsp-mount.service
                    ln -sf  ../dsp-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/dsp-mount.service
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
                    install -m 0744 ${WORKDIR}/systemd/firmware-ubi-mount.sh ${D}${sysconfdir}/initscripts/firmware-ubi-mount.sh
                    ln -sf  ../firmware-mount.service  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/firmware-mount.service
                    fi
                fi
                if [ "$d" == "/dsp" ]; then
                    install -m 0644 ${WORKDIR}/systemd/dsp.mount ${D}${sysconfdir}/systemd/system/dsp.mount
                    ln -sf  ../dsp.mount  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/dsp.mount
                fi
            fi
            # systemrw is applicable only when rootfs is read only.
            if ${@bb.utils.contains('DISTRO_FEATURES','ro-rootfs','true','false',d)}; then
                if [ "$d" == "/systemrw" ]; then
                    if ${@bb.utils.contains('DISTRO_FEATURES','nand-boot','false','true',d)}; then
                    install -m 0644 ${WORKDIR}/systemd/systemrw.mount ${D}${sysconfdir}/systemd/system/systemrw.mount
                    else
                    install -m 0644 ${WORKDIR}/systemd/systemrw-ubi.mount ${D}${sysconfdir}/systemd/system/systemrw.mount
                    fi
                    ln -sf  ../systemrw.mount  ${D}${sysconfdir}/systemd/system/local-fs.target.requires/systemrw.mount
                fi
            fi
        done
    fi
}
