SUMMARY = "Multimedia libraries and SDK"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/media/.git;protocol=${PROTO};destsuffix=hardware/qcom/media;usehead=1"
S = "${WORKDIR}/hardware/qcom/media"
SRCREV = "${AUTOREV}"


PR = "r1"

DEPENDS = "virtual/kernel"
DEPENDS += "glib-2.0"
DEPENDS += "virtual/libc"
DEPENDS += "libion libcutils libutils system-core-headers"
DEPENDS += "adreno-headers"
DEPENDS += "mm-video-noship"
DEPENDS += "libdrm gbm adreno wayland gbm-headers"
DEPENDS += "display-commonsys-intf-linux display-hal-headers"
DEPENDS += "media-plugin-headers"

# Need the kernel headers
PACKAGE_ARCH = "${MACHINE_ARCH}"

LV = "1.0.0"

inherit autotools


#re-use non-perf settings
EXTRA_OECONF_append =" --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF_append =" --with-kernel-headers=${STAGING_KERNEL_BUILDDIR}/include"
EXTRA_OECONF_append =" --with-adreno-includes=${STAGING_INCDIR}/adreno"

EXTRA_OECONF_append =" --with-cutils-headers=${STAGING_INCDIR}/cutils/"
EXTRA_OECONF_append =" --with-log-headers=${STAGING_INCDIR}/log/"
EXTRA_OECONF_append =" --with-usr-include-headers=${STAGING_INCDIR}/"

EXTRA_OECONF_append =" --enable-use-glib='yes'"
EXTRA_OECONF_append =" --enable-target-uses-ion='yes'"
EXTRA_OECONF_append =" --enable-target-uses-gbm='yes'"
EXTRA_OECONF_append =" --enable-target-uses-media-extensions='no'"
EXTRA_OECONF_append =" --enable-build-mm-video='yes'"
EXTRA_OECONF_append =" --enable-is-ubwc-supported='yes'"
EXTRA_OECONF_append =" --enable-build-swcodec='yes'"
EXTRA_OECONF_append =" --enable-target-output-deinterlaced='yes'"
EXTRA_OECONF_append ="${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', ' --enable-target-hypervisor=yes', '', d)}"

CPPFLAGS += "-I${STAGING_INCDIR} \
             -I${STAGING_INCDIR}/drm \
             -I${STAGING_INCDIR}/EGL \
             -I${STAGING_INCDIR}/GLES2 \
             -I${STAGING_INCDIR}/glib-2.0 \
             -I${STAGING_LIBDIR}/glib-2.0/include \
             -I${STAGING_LIBDIR}/glib-2.0/glib \
             -I${STAGING_INCDIR}/c++ \
             -I${STAGING_INCDIR}/c++/${TARGET_SYS} \
             -I${STAGING_INCDIR}/ion_headers  \
             -I${STAGING_INCDIR}/disp-commonsys-intf/display \
             -I${STAGING_INCDIR}/mm-video/swvdec \
             -I${STAGING_INCDIR}/mm-video/swvenc"

CPPFLAGS += "-include stdint.h"

LDFLAGS += "-lglib-2.0"
LDFLAGS += "-lgbm"
LDFLAGS += "-ldrm"
LDFLAGS += "-lwayland-client"
LDFLAGS += "-lEGL"
LDFLAGS += "-lqdMetaData"

do_install_append() {
   install -d ${D}${includedir}/mm-core
   install -m 0644 ${S}/mm-core/inc/*.h -D ${D}${includedir}/mm-core/
   install -d ${D}${includedir}/venc/inc
   install -m 0644 ${S}/mm-video-v4l2/vidc/venc/inc/omx_video_common.h -D ${D}${includedir}/venc/inc/
   install -m 0644 ${S}/mm-video-v4l2/vidc/venc/inc/omx_video_base.h -D ${D}${includedir}/venc/inc/

   install -d ${D}${includedir}/vdec/inc
   install -m 0644 ${S}/mm-video-v4l2/vidc/vdec/inc/*.h -D ${D}${includedir}/vdec/inc/

   install -m 0644 ${S}/mm-video-v4l2/vidc/common/inc/*.h -D ${D}${includedir}/
   install -d ${D}${includedir}/libstagefrighthw
   install -m 0644 ${S}/libstagefrighthw/QComOMXMetadata.h -D ${D}${includedir}/libstagefrighthw/
   install -m 0644 ${S}/libc2dcolorconvert/C2DColorConverter.h ${D}${includedir}/
   install -d ${D}${includedir}/libarbitrarybytes
   install -m 0644 ${S}/libarbitrarybytes/inc/*.h -D ${D}${includedir}/libarbitrarybytes/

}

FILES_${PN}-dev = "\
    ${includedir} \
    ${libdir}/*.la"

FILES_${PN} = "\
    ${libdir}/* \
    ${bindir}/* \
    ${datadir}/*"

#Skips check for .so symlinks
INSANE_SKIP_${PN} = "dev-so"
FILES_${PN} += "${includedir}/mm-core/* \
                ${includedir}/libstagefrighthw/* \
                ${includedir}/venc/inc/* \
                ${includedir}/vdec/inc/* \
                ${includedir}/*"
