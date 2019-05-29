# 1. Update libdrm source code to QTI version
# 2. Add a patch to include <sys/sysmacros.h> for major() and minor() defination

FILESEXTRAPATHS_prepend := "${WORKSPACE}/graphics/:${THISDIR}/${PN}:"
SRC_URI = "file://libdrm/"
SRC_URI_append = " file://0001-include-sys-sysmacros.h-for-major-minor-definations.patch "

S = "${WORKDIR}/libdrm"
