inherit autotools pkgconfig systemd useradd distro_features_check

REQUIRED_DISTRO_FEATURES = "systemd"

DESCRIPTION = "Android system/core components"
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI   =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1"
S = "${WORKDIR}/system/core/"
SRCREV = "${AUTOREV}"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append = " file://0001-Fix-adb-shell-env-issue.patch"

PR = "r19"

DEPENDS += "virtual/kernel openssl glib-2.0 libselinux ext4-utils libcutils libmincrypt libbase libutils"

EXTRA_OECONF = " --with-host-os=${HOST_OS} --with-glib"
EXTRA_OECONF_append = " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF_append = " --disable-debuggerd"
EXTRA_OECONF_append = " --disable-libsync"

# Disable adb root privileges in USER builds for msm targets
EXTRA_OECONF_append_msm = "${@bb.utils.contains('VARIANT','user',' --disable-adb-root','',d)}"

CPPFLAGS += "-I${STAGING_INCDIR}/ext4_utils"
CPPFLAGS += "-I${STAGING_INCDIR}/libselinux"

COMPOSITION = "901D"

QPERM_SERVICE = "${S}/leproperties/leprop.service"

do_install_append() {
   install -m 0755 ${S}/adb/launch_adbd -D ${D}${sysconfdir}/launch_adbd
   install -b -m 0644 /dev/null ${D}${sysconfdir}/adb_devid
   install -d ${D}${sysconfdir}/usb/
   install -b -m 0644 /dev/null ${D}${sysconfdir}/usb/boot_hsusb_comp
   install -b -m 0644 /dev/null ${D}${sysconfdir}/usb/boot_hsic_comp
   echo ${COMPOSITION} > ${D}${sysconfdir}/usb/boot_hsusb_comp
   install -m 0755 ${S}/usb/usb_composition -D ${D}${base_sbindir}/
   install -d ${D}${base_sbindir}/usb/compositions/
   install -m 0755 ${S}/usb/compositions/* -D ${D}${base_sbindir}/usb/compositions/
   install -m 0755 ${S}/usb/target -D ${D}${base_sbindir}/usb/
   install -d ${D}${base_sbindir}/usb/debuger/
   install -m 0755 ${S}/usb/debuger/debugFiles -D ${D}${base_sbindir}/usb/debuger/
   install -m 0755 ${S}/usb/debuger/help -D ${D}${base_sbindir}/usb/debuger/
   install -m 0755 ${S}/usb/debuger/usb_debug -D ${D}${base_sbindir}/
   install -b -m 0644 /dev/null -D ${D}${sysconfdir}/build.prop
   if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
      install -m 0750 ${S}/adb/start_adbd -D ${D}${sysconfdir}/initscripts/adbd
      install -m 0755 ${S}/usb/start_usb -D ${D}${sysconfdir}/initscripts/usb
      install -m 0750 ${S}/rootdir/etc/init.qcom.post_boot.sh -D ${D}${sysconfdir}/initscripts/init_post_boot
      install -m 0750 ${S}/rootdir/etc/init.qti.early_boot.sh -D ${D}${sysconfdir}/initscripts/init_early_boot
      install -d ${D}${systemd_unitdir}/system/
      install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
      install -d ${D}${systemd_unitdir}/system/sysinit.target.wants/
      install -m 0644 ${S}/adb/adbd.service -D ${D}${systemd_unitdir}/system/adbd.service
      ln -sf ${systemd_unitdir}/system/adbd.service ${D}${systemd_unitdir}/system/multi-user.target.wants/adbd.service
      install -m 0644 ${S}/usb/usb.service -D ${D}${systemd_unitdir}/system/usb.service
      ln -sf ${systemd_unitdir}/system/usb.service ${D}${systemd_unitdir}/system/multi-user.target.wants/usb.service
      install -m 0644 ${S}/rootdir/etc/init_post_boot.service -D ${D}${systemd_unitdir}/system/init_post_boot.service
      ln -sf ${systemd_unitdir}/system/init_post_boot.service \
          ${D}${systemd_unitdir}/system/multi-user.target.wants/init_post_boot.service
      install -m 0644 ${S}/rootdir/etc/init_early_boot.service -D ${D}${systemd_unitdir}/system/init_early_boot.service
      ln -sf ${systemd_unitdir}/system/init_early_boot.service \
          ${D}${systemd_unitdir}/system/sysinit.target.wants/init_early_boot.service
      install -m 0644 ${S}/leproperties/leprop.service -D ${D}${systemd_unitdir}/system/leprop.service
      ln -sf ${systemd_unitdir}/system/leprop.service \
          ${D}${systemd_unitdir}/system/multi-user.target.wants/leprop.service

      # update usb.service to depend on var-volatile.mount
      sed -i -e '/^After/d' ${D}${systemd_unitdir}/system/usb.service
      sed -i -e '/^Requires/d' ${D}${systemd_unitdir}/system/usb.service
      sed -i -e '/^Descr/a\RequiresMountsFor=\/var' ${D}${systemd_unitdir}/system/usb.service
      sed -i -e '/^Descr/a\Requires=var-adb_devid.service var-usb.service' ${D}${systemd_unitdir}/system/usb.service
      sed -i -e '/^Descr/a\After=var-volatile.mount leprop.service' ${D}${systemd_unitdir}/system/usb.service
      sed -i -e '/^ExecStartPre/d' ${D}${systemd_unitdir}/system/usb.service
      sed -i -e '/^Descr/a\ConditionVirtualization=!container' ${D}${systemd_unitdir}/system/usb.service
   fi

    # remove headers, use system-core-headers recipe
    rm -rf ${D}${includedir}
}

PACKAGES =+ "${PN}-adbd-dbg ${PN}-adbd ${PN}-adbd-dev"
FILES_${PN}-adbd-dbg = "${base_sbindir}/.debug/adbd ${libdir}/.debug/libadbd.*"
FILES_${PN}-adbd     = "${base_sbindir}/adbd ${sysconfdir}/init.d/adbd ${libdir}/libadbd.so.*"
FILES_${PN}-adbd    += "${systemd_unitdir}/system/adbd.service ${systemd_unitdir}/system/multi-user.target.wants/adbd.service ${systemd_unitdir}/system/local-fs.target.wants/adbd.service ${systemd_unitdir}/system/ffbm.target.wants/adbd.service ${sysconfdir}/launch_adbd ${sysconfdir}/initscripts/adbd ${sysconfdir}/adb_devid"
FILES_${PN}-adbd-dev = "${libdir}/libadbd.so ${libdir}/libadbd.la"

PACKAGES =+ "${PN}-usb-dbg ${PN}-usb"
FILES_${PN}-usb-dbg  = "${bindir}/.debug/usb_composition_switch"
FILES_${PN}-usb      = "${sysconfdir}/init.d/usb ${base_sbindir}/usb_composition ${bindir}/usb_composition_switch ${base_sbindir}/usb/compositions/*"
FILES_${PN}-usb     += "${sysconfdir}/usb/*"
FILES_${PN}-usb     += "${base_sbindir}/usb/* ${base_sbindir}/usb_debug ${base_sbindir}/usb/debuger/*"
FILES_${PN}-usb     += "${systemd_unitdir}/system/usb.service ${systemd_unitdir}/system/multi-user.target.wants/usb.service ${systemd_unitdir}/system/local-fs.target.wants/usb.service ${systemd_unitdir}/system/ffbm.target.wants/usb.service ${sysconfdir}/initscripts/usb"

PACKAGES =+ "${PN}-post-boot"
FILES_${PN}-post-boot  = "${sysconfdir}/init.d/init_post_boot"
FILES_${PN}-post-boot += "${systemd_unitdir}/system/init_post_boot.service ${systemd_unitdir}/system/multi-user.target.wants/init_post_boot.service ${systemd_unitdir}/system/ffbm.target.wants/init_post_boot.service ${sysconfdir}/initscripts/init_post_boot"

PACKAGES =+ "${PN}-early-boot"
FILES_${PN}-early-boot  = "${sysconfdir}/init.d/init_early_boot"
FILES_${PN}-early-boot += "${systemd_unitdir}/system/init_early_boot.service ${systemd_unitdir}/system/sysinit.target.wants/init_early_boot.service ${sysconfdir}/initscripts/init_early_boot"

PACKAGES =+ "${PN}-leprop-dbg ${PN}-leprop"
FILES_${PN}-leprop-dbg  = "${base_sbindir}/.debug/leprop-service ${bindir}/.debug/getprop ${bindir}/.debug/setprop"
FILES_${PN}-leprop      = "${base_sbindir}/leprop-service ${bindir}/getprop ${bindir}/setprop ${sysconfdir}/proptrigger.sh ${sysconfdir}/proptrigger.conf"
FILES_${PN}-leprop     += "${systemd_unitdir}/system/leprop.service ${systemd_unitdir}/system/multi-user.target.wants/leprop.service ${systemd_unitdir}/system/ffbm.target.wants/leprop.service ${sysconfdir}/build.prop"

FILES_${PN}-dbg  = "${bindir}/.debug/* ${libdir}/.debug/*"
FILES_${PN}      = "${bindir}/* ${libdir}/pkgconfig/* ${libdir}/*.so.* "
FILES_${PN}-dev  = "${libdir}/*.so ${libdir}/*.la ${includedir}*"
