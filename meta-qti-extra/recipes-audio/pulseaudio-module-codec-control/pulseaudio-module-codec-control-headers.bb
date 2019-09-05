
DESCRIPTION = "Pluseaudio codec control module header files"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
PR = "r0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/mm-audio/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/mm-audio/pulseaudio-module-codec-control;subpath=pulseaudio-module-codec-control;usehead=1"
SRCREV  = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/mm-audio/pulseaudio-module-codec-control/"


do_install() {
    install -d ${D}${includedir}
    cp -r ${S}/inc/interface/*  ${D}${includedir}
}

ALLOW_EMPTY_${PN} = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
