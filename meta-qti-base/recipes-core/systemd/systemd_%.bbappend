FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://0001-systemd-add-slotselect-support-in-fstab.patch "
SRC_URI += " file://0033-systemd-Make-root-s-home-directory-configurable-2.patch "
SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'lxc', ' file://0001-systemd-skip-smack-copy-issue-in-systemd.patch ', '', d)}"


# Remove backlight ldconfig
#   * backlight - Loads/Saves Screen Backlight Brightness, not required.
#   * ldconfig  - configures dynamic linker run-time bindings.
#                 ldconfig  creates  the  necessary links and cache to the most
#                 recent shared libraries found in the directories specified on
#                 the command line, in the file /etc/ld.so.conf, and in the
#                 trusted directories (/lib and /usr/lib).  The cache (created
#                 at /etc/ld.so.cache) is used by the run-time linker ld-linux.so.
#                 system-ldconfig.service runs "ldconfig -X", but as / is read-only
#                 cache may not be created. Disabling this may introduce app
#                 start time latency.
PACKAGECONFIG_remove = " backlight ldconfig "

CFLAGS_append = " -fPIC"

# In aarch64 targets systemd is not booting with -finline-functions -finline-limit=64 optimizations
# So temporarily revert to default optimizations for systemd.
FULL_OPTIMIZATION = "-O2 -fexpensive-optimizations -frename-registers -fomit-frame-pointer -ftree-vectorize"

do_install_append () {
    # Use kernel rules for network iface name
    sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}/lib/systemd/network/99-default.link

    #Remove privatetmp=true from hostname service
    sed -i  '/^PrivateTmp.*/d' ${D}/lib/systemd/system/systemd-hostnamed.service

    # Remove orignal 60-persistent-v4l.rules which is not applicable for QTI video
    rm ${D}/lib/udev/rules.d/60-persistent-v4l.rules
}

# disable weston in host for multi-drm support
pkg_postinst_${PN} () {
  if ${@bb.utils.contains('DISTRO_FEATURES','lxc','true','false',d)}; then
    if [ -n "$D" ]; then
      OPTS="--root=$D"
    fi
    systemctl $OPTS mask weston.service
    systemctl $OPTS mask servicemanager.service
  fi
}
