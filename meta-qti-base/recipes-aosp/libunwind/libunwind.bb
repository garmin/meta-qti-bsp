inherit autotools-brokensep pkgconfig

DESCRIPTION = "Libunwind"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
DEPENDS = "libatomic-ops"

PR = "r0"
SRC_URI   =  "${PATH_TO_REPO}/external/libunwind/.git;protocol=${PROTO};destsuffix=external/libunwind;usehead=1"
S = "${WORKDIR}/external/libunwind"

SRCREV = "${AUTOREV}"
