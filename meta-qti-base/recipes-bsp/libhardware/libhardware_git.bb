inherit autotools pkgconfig

DESCRIPTION = "hardware libhardware headers"
HOMEPAGE = "http://codeaurora.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI   =  "${PATH_TO_REPO}/hardware/libhardware/.git;protocol=${PROTO};destsuffix=hardware/libhardware;nobranch=1"
# Get Add-gralloc1.h-from-p-keystone-qcom-branch
SRC_URI_append = " https://source.codeaurora.org/quic/le/platform/hardware/libhardware/plain/include/hardware/gralloc1.h?h=keystone/p-keystone-qcom-release;downloadfilename=gralloc1.h;md5sum=5171fc33c1299824ede5756a4da57507"

SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/hardware/libhardware', d)}"
S = "${WORKDIR}/hardware/libhardware"

PR = "r6"

DEPENDS += "libcutils liblog system-core"

EXTRA_OECONF_append_apq8053 = " --enable-sensors"
EXTRA_OECONF_append_concam = " --enable-camera"
EXTRA_OECONF_append_sdm845 = " --enable-sensors"
EXTRA_OECONF_append_sdm845 = " --enable-camera"
EXTRA_OECONF_append_robot-som = " --enable-camera"

do_install_append () {
    cp ${WORKDIR}/gralloc1.h ${D}${includedir}/hardware/
}
