# 1. Update libdrm source code to QTI version
# 2. Add a patch to include <sys/sysmacros.h> for major() and minor() defination

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI = "${PATH_TO_REPO}/graphics/libdrm/.git;protocol=${PROTO};destsuffix=graphics/libdrm;usehead=1"
SRC_URI_append = " file://0001-include-sys-sysmacros.h-for-major-minor-definations.patch "

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/graphics/libdrm"
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', '--enable-drm_fe=yes', '', d)}"
