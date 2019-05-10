FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_remove = " file://backport-v234-e266c06.patch "
SRC_URI += " file://0001-systemd-add-slotselect-support-in-fstab.patch "
SRC_URI += " file://0032-systemd-add-bootkpi-marker-for-user-session.patch "

CFLAGS_append = " -fPIC"
# In aarch64 targets systemd is not booting with -finline-functions -finline-limit=64 optimizations
# So temporarily revert to default optimizations for systemd.
FULL_OPTIMIZATION = "-O2 -fexpensive-optimizations -frename-registers -fomit-frame-pointer -ftree-vectorize"

do_install_append () {
   # Use kernel rules for network iface
   sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}/lib/systemd/network/99-default.link
   sed -i  '/^PrivateTmp.*/d' ${D}/lib/systemd/system/systemd-hostnamed.service

   # Remove orignal 60-persistent-v4l.rules
   rm ${D}/lib/udev/rules.d/60-persistent-v4l.rules
}
