# Tune default conman config file

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI = "file://main.conf"

FILES_${PN} += "${sysconfdir}/connman/*"

do_install_append () {
  install -d ${D}${sysconfdir}/connman/
  install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/
  # For early_ethernet, eth is configure in early_init. Just let connman ignore it.
  if ${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', 'true', 'false', d)}; then
      sed -i '/^NetworkInterfaceBlacklist/s/$/,eth0,eth1/' ${D}${sysconfdir}/connman/main.conf
  elif ${@bb.utils.contains('DISTRO_FEATURES', 'early-eth', 'true', 'false', d)}; then
      sed -i '/^NetworkInterfaceBlacklist/s/$/,eth0/' ${D}${sysconfdir}/connman/main.conf
  fi
}
