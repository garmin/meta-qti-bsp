FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://0001-systemd-add-slotselect-support-in-fstab.patch "
SRC_URI += " file://0033-systemd-Make-root-s-home-directory-configurable-2.patch "

do_install_append () {
    # Use kernel rules for network iface
    sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}/lib/systemd/network/99-default.link
    sed -i  '/^PrivateTmp.*/d' ${D}/lib/systemd/system/systemd-hostnamed.service

    # Remove orignal 60-persistent-v4l.rules
    rm ${D}/lib/udev/rules.d/60-persistent-v4l.rules
}
