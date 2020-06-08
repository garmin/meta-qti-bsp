inherit qcommon cmake
DESCRIPTION = "OMX video encoder lite sample"
SECTION  = "mm-venc-omx-test-lite"

FILESPATH  =+ "${WORKSPACE}:"

SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRC_DIR = "${SRC_DIR_ROOT}/gstreamer/gst-plugins-qti-oss/omx-lite-app/mm-venc-omx-test"
S      = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/omx-lite-app/mm-venc-omx-test"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "glib-2.0"
DEPENDS += "libcutils"
DEPENDS += "media"
DEPENDS += "virtual/kernel"

CFLAGS += "-include stdbool.h"
CFLAGS += "-include stdint.h"
CFLAGS += "-include signal.h"
CFLAGS += "-include stdio.h"
CXXFLAGS += "${CFLAGS}"
CXXFLAGS += "-I${STAGING_INCDIR}/glib-2.0"
CXXFLAGS += "-I${STAGING_INCDIR}/glib-2.0/include"
CXXFLAGS += "-I${STAGING_INCDIR}/glib-2.0/glib"
CXXFLAGS += "-I${STAGING_INCDIR}/c++"
CXXFLAGS += "-I${STAGING_INCDIR}/c++/${TARGET_SYS}"
CXXFLAGS += "-I${STAGING_INCDIR}/common/inc"
CXXFLAGS += "-I${STAGING_INCDIR}/mm-osal/include"
CXXFLAGS += "-I${STAGING_INCDIR}/mm-core/include"
CXXFLAGS += "-I${STAGING_INCDIR}/mm-core"
CXXFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CXXFLAGS += "-I${STAGING_INCDIR}/ion_headers"

FILES_${PN} += " \
   ${libdir}/* \
   ${bindir}/* \
   ${includedir}/* \
"
