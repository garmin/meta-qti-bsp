inherit autotools

DESCRIPTION = "Boot control"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
HOMEPAGE = "https://source.codeaurora.org/quic/la/platform/hardware/qcom/bootctrl/"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

SRC_URI = "git://source.codeaurora.org/quic/la/platform/hardware/qcom/bootctrl;branch=android-blast.lnx.3.0;protocol=https"
SRC_URI_append = " file://0001-bootcontrol-add-autotool-support.patch \
                   file://0002-bootcontrol-update-bootdev-dir.patch "

SRCREV = "99c70acec8f8f8653aedfd6b968b66e3e6c77ad3"

S = "${WORKDIR}/git"

PR = "r1"

DEPENDS += "oem-recovery libhardware liblog libcutils"
RDEPENDS_${PN} += "oem-recovery"
