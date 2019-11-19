SUMMARY = "Container config file"
DESCRIPTION = "Sample container config file"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

inherit autotools  pkgconfig

FILESEXTRAPATHS_append := ":${THISDIR}"

SRC_URI = "file://${PN}"

# SRC_URI = "file://${THISDIR}/${PN}/ivi1/config"
# SRC_URI = "file://${THISDIR}/${PN}/ivi1/config"

S = "${WORKDIR}/${PN}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${localstatedir}/lib/lxc/
    cp -rf ${S}/* ${D}/var/lib/lxc/
}


FILES_${PN} = "${localstatedir}/*"
