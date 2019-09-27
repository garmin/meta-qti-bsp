DESCRIPTION = "hardware libhardware headers"
HOMEPAGE = "http://codeaurora.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI   =  "${PATH_TO_REPO}/hardware/libhardware/.git;protocol=${PROTO};destsuffix=hardware/libhardware;usehead=1"
# Get Add-gralloc1.h-from-p-keystone-qcom-branch
SRC_URI_append = " https://source.codeaurora.org/quic/le/platform/hardware/libhardware/plain/include/hardware/gralloc1.h?h=keystone/p-keystone-qcom-release;downloadfilename=gralloc1.h;md5sum=5171fc33c1299824ede5756a4da57507"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/hardware/libhardware"

PR = "r1"

DEPENDS = "system-core-headers"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    install -d ${D}${includedir}/hardware/
    install -m 0644 ${WORKDIR}/gralloc1.h ${D}${includedir}/hardware/
    install -m 0644 ${S}/include/hardware/*.h ${D}${includedir}/hardware/
}
