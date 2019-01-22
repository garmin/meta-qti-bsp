inherit autotools-brokensep pkgconfig update-rc.d
PR = "r7"

DESCRIPTION = "Recovery bootloader"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
HOMEPAGE = "https://www.codeaurora.org/gitweb/quic/la?p=platform/bootable/recovery.git"
DEPENDS = "glib-2.0 libmincrypt-native system-core oem-recovery libsparse"
RDEPENDS_${PN} = "zlib bzip2"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://OTA/recovery/"
SRC_URI += "file://poky/meta-qti-bsp/recipes-bsp/recovery/files/recovery.service"

S = "${WORKDIR}/OTA/recovery/"

EXTRA_OECONF = "--with-glib --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
                --with-core-headers=${STAGING_INCDIR}"
CFLAGS += "-lsparse -llog"

SYSTEMD_SUPPORT = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)}"

SELINUX_SUPPORT = "${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'selinux', '', d)}"

AB_SUPPORT = "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'TARGET_SUPPORTS_AB=true', '', d)}"
NEED_ABCTL = "${@bb.utils.contains('DISTRO_FEATURES', 'ab-boot-support', 'abctl', '', d)}"
EXTRA_OECONF += " ${AB_SUPPORT}"
DEPENDS += " ${NEED_ABCTL}"

PARALLEL_MAKE = ""
INITSCRIPT_NAME = "recovery"
INITSCRIPT_PARAMS = "start 99 5 . stop 80 0 1 6 ."

FILES_${PN}  = "${libdir} ${sysconfdir}"
FILES_${PN} += "/cache"
FILES_${PN} += "/data"
FILES_${PN} += "/res"
FILES_${PN} += "/system"
#FILES_${PN} += "/tmp"
FILES_${PN} += "/res"
FILES_${PN} += "/data"
FILES_${PN} += "/lib"

PACKAGES += "${PN}-bin"
FILES_${PN}-bin += "${bindir}"

do_install_append() {
        install -d ${D}/cache/
#        install -d ${D}/tmp/
        install -d ${D}/res/
        install -d ${D}/data/
        install -d ${D}/system/
        install -m 0755 ${S}/start_recovery -D ${D}${sysconfdir}/init.d/recovery

        if [ "${AB_SUPPORT}" == "TARGET_SUPPORTS_AB=true" ]; then
              # If A/B boot is supported, recovery is just an executable and not a service
              # Just install the fstab for recovery
              install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/base-files-recovery/fstab_AB -D ${D}/res/recovery_volume_config
        else
              if [ "${SYSTEMD_SUPPORT}" == "systemd" ]; then
                    install -d ${D}${systemd_unitdir}/system/
                    install -m 0644 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/recovery/files/recovery.service -D ${D}${systemd_unitdir}/system/recovery.service
                    install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
                    # enable the service for multi-user.target
                    ln -sf ${systemd_unitdir}/system/recovery.service \
                                  ${D}${systemd_unitdir}/system/multi-user.target.wants/recovery.service
              fi
              if [ "${SELINUX_SUPPORT}" == "selinux" ]; then
                  install -m  0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/base-files-recovery/fstab -D ${D}${sysconfdir}/fstab
                  install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/base-files-recovery/fstab -D ${D}/res/recovery_volume_config
              else
                  install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/base-files-recovery/fstab_noselinux -D ${D}${sysconfdir}/fstab
                  install -m 0755 ${WORKSPACE}/poky/meta-qti-bsp/recipes-bsp/base-files-recovery/fstab_noselinux -D ${D}/res/recovery_volume_config
              fi
        fi
}
