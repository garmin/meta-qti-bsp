inherit module module-sign qperf

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

DESCRIPTION = "QTI Audio drivers"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

PR = "r0"

DEPENDS = "virtual/kernel"
AUDIO_BUILD_TARGET = "auto-audio-target"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-kernel/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-kernel;usehead=1"
SRC_URI_append = " file://${AUDIO_BUILD_TARGET}/"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"

FILES_${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
FILES_${PN} += "${sysconfdir}/*"
FILES_${PN} +="/etc/initscripts/start_audio_le"
FILES_${PN} += "${systemd_unitdir}/system/audio.service"
FILES_${PN} += "${systemd_unitdir}/system/multi-user.target.wants/audio.service"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

# Disable parallel make
PARALLEL_MAKE = ""

do_configure() {
  cp -f ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile
}

INITSCRIPT_NAME = "start_audio_le"
INITSCRIPT_PARAMS = "start 35 5 . stop 15 0 1 6 ."

do_install_append() {
  install -d ${D}${includedir}/audio-kernel/
  install -d ${D}${includedir}/audio-kernel/linux
  install -d ${D}${includedir}/audio-kernel/linux/mfd
  install -d ${D}${includedir}/audio-kernel/linux/mfd/wcd9xxx
  install -d ${D}${includedir}/audio-kernel/sound
  install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

  cp -fr ${S}/linux/* ${D}${includedir}/audio-kernel/linux
  install -m 0644 ${S}/sound/* ${D}${includedir}/audio-kernel/sound

  if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
  install -m 0755 ${WORKDIR}/${AUDIO_BUILD_TARGET}/audio_load.conf -D ${D}${sysconfdir}/modules-load.d/audio_load.conf
  else
    install -m 0755 ${WORKDIR}/${AUDIO_BUILD_TARGET}/audio_load.conf -D ${D}${sysconfdir}/modules/audio_load.conf
  fi

   for i in $(find ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/. -name "*.ko"); do
   mv ${i} ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
   done

   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/asoc
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/dsp
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/ipc
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/soc
}

do_install_append_mdm() {
  install -m 0755 ${WORKDIR}/${AUDIO_BUILD_TARGET}/audio_load.conf -D ${D}${sysconfdir}/modprobe.d/audio_load.conf
  install -d ${D}${sysconfdir}/initscripts
  install -m 0755 ${WORKDIR}/${AUDIO_BUILD_TARGET}/start_audio_le ${D}${sysconfdir}/initscripts
  install -m 0644 ${WORKDIR}/${AUDIO_BUILD_TARGET}/audio.service -D ${D}${systemd_unitdir}/system/audio.service
  install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
# enable the service for multi-user.target
   ln -sf ${systemd_unitdir}/system/audio.service \
   ${D}${systemd_unitdir}/system/multi-user.target.wants/audio.service
}

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.

RPROVIDES_${PN} += "${@'kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-apr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-bolero-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-csra66x0-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-va-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wsa-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-cpe-lsm-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-machine-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-native-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-pinctrl-lpi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-pinctrl-wcd-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-platform-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-q6-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-q6-pdr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-swr-ctrl-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-swr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-usf-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wglink-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-analog-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-digital-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-msm-sdw-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wcd934x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-hdmi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-ep92-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-machine-ext-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-machine-ext-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-mbhc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-stub-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wcd-core-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wcd-cpe-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wcd9335-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wcd9xxx-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wsa881x-analog-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wsa881x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wcd-spi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-machine-int-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-snd-event-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-rx-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-tx-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wcd937x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-wcd937x-slave-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"

