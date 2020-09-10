inherit qcommon
DESCRIPTION = "Provide display-hal Headers"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/display/display-hal/.git;protocol=${PROTO};destsuffix=display/display-hal;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/display/display-hal"


do_install() {
    install -d ${D}${includedir}
    install ${S}/include/*.h ${D}${includedir}
    install ${S}/libqservice/*.h ${D}${includedir}
}

ALLOW_EMPTY_${PN} = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
