# Force security-manager policy generated at first boot
pkg_postinst_${PN}-policy () {
    # Fail on error.
    set -e

    if [ ! -e "$D/var/local/db/security-manager" ]; then
        mkdir -p $D/var/local/db
#        cp -ra $D/usr/dbspace/ $D/var/local/db/security-manager
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        if [ -n "$D" ]; then
            OPTS="--root=$D"
        fi
        systemctl $OPTS disable init-security-manager-db.service
    fi
    exit 0
}
