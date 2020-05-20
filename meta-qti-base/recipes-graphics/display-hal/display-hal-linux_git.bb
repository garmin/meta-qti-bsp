inherit autotools qcommon

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

PACKAGES = "${PN}"

SRC_DIR = "${SRC_DIR_ROOT}/display/display-hal"
SRC_URI = "${PATH_TO_REPO}/display/display-hal/.git;protocol=${PROTO};destsuffix=display/display-hal;usehead=1"
S = "${WORKDIR}/display/display-hal"
SRCREV = "${AUTOREV}"

DEPENDS += "system-core"
DEPENDS += "libhardware"
DEPENDS += "binder"
DEPENDS += "drm"
DEPENDS += "libdrm"
#DEPENDS += "adreno"
DEPENDS += "gbm-headers"
DEPENDS += "display-commonsys-intf-linux"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

EXTRA_OECONF += " --enable-sdmhaldrm"
LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS += "-DCOMPILE_DRM"
CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libdrmutils"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libqdutils"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libqservice"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/sdm/include"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/include"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libdebug"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/gralloc"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"

# fix for uapi msm_drm.h header file related compilation issue
CPPFLAGS += "-fno-operator-names"

do_install_append () {
    # libhardware expects to find /usr/lib/hw/gralloc.*.so
    install -d ${D}${libdir}/hw
    ln -s ${libdir}/libgralloc.so ${D}${libdir}/hw/gralloc.default.so
    cp -fR ${WORKDIR}/display/display-hal/include/* ${STAGING_INCDIR}
    cp -fR ${WORKDIR}/display/display-hal/gpu_tonemapper/*.h ${STAGING_INCDIR}
}

FILES_${PN} = "${libdir}/*.so"
FILES_${PN} += "/usr/lib/hw/gralloc.default.so"

FILES_${PN} += " \
   ${libdir}/* \
   ${includedir} \
   ${includedir}/utils \
   ${includedir}/private \
   ${includedir}/core \
"

PACKAGES = "${PN}"
INSANE_SKIP_${PN} = "dev-so"
INHIBIT_PACKAGE_STRIP="1"
INHIBIT_PACKAGE_DEBUG_SPLIT="1"
