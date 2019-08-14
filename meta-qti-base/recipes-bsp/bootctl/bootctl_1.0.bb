inherit autotools 

DESCRIPTION = "bootctrl  utility."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

FILESEXTRAPATHS_prepend := "${THISDIR}:"

SRC_URI = "https://source.codeaurora.org/quic/la/platform/system/extras/plain/bootctl/bootctl.cpp?h=LA.AU.0.0.1_rb1.22;downloadfilename=bootctl.cpp;md5sum=093f5752c62bc002fe4d6291dffaf289"
SRC_URI += " file://0001-Enabel-bootctl-for-LV.patch "

S = "${WORKDIR}/${PN}"

do_move_bootctl() {
    cp ${WORKDIR}/bootctl.cpp ${S}
}
do_patch[prefuncs] += "do_move_bootctl"

DEPENDS += "oem-recovery libhardware liblog libcutils libbootctrl"
RDEPENDS_${PN} += "libbootctrl"
