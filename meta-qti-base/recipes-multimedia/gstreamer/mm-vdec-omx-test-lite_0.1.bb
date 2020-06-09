inherit qcommon cmake
DESCRIPTION = "OMX video decoder lite sample"
SECTION  = "mm-vdec-omx-test-lite"

SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gst-plugins-qti-oss/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-qti-oss;usehead=1"
SRC_DIR = "${SRC_DIR_ROOT}/gstreamer/gst-plugins-qti-oss/omx-lite-app/mm-vdec-omx-test"
S      = "${WORKDIR}/gstreamer/gst-plugins-qti-oss/omx-lite-app/mm-vdec-omx-test"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "glib-2.0"
DEPENDS += "libion libcutils"
DEPENDS += "weston"
DEPENDS += "media"
DEPENDS += "virtual/kernel"

CFLAGS += "-include stdbool.h"
CFLAGS += "-include stdint.h"
CFLAGS += "-include signal.h"
CFLAGS += "-include stdio.h"
CXXFLAGS += "${CFLAGS}"
CXXFLAGS += "-I${STAGING_INCDIR}"
CXXFLAGS += "-I${STAGING_INCDIR}/drm"
CXXFLAGS += "-I${STAGING_INCDIR}/EGL"
CXXFLAGS += "-I${STAGING_INCDIR}/GLES2"
CXXFLAGS += "-I${STAGING_INCDIR}/glib-2.0"
CXXFLAGS += "-I${STAGING_LIBDIR}/glib-2.0/include"
CXXFLAGS += "-I${STAGING_LIBDIR}/glib-2.0/glib"
CXXFLAGS += "-I${STAGING_INCDIR}/c++"
CXXFLAGS += "-I${STAGING_INCDIR}/c++/${TARGET_SYS}"
CXXFLAGS += "-I${STAGING_INCDIR}/common/inc"
CXXFLAGS += "-I${STAGING_INCDIR}/mm-core"
CXXFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
CXXFLAGS += "-I${STAGING_INCDIR}/disp-commonsys-intf/display"
CXXFLAGS += "-I${STAGING_INCDIR}/ion_headers"

FILES_${PN} += " \
   ${libdir}/* \
   ${bindir}/* \
   ${includedir}/* \
"
