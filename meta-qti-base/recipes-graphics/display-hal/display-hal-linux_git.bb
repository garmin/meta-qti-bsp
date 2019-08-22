inherit autotools qcommon

DESCRIPTION = "display Library"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r8"

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

    cd ${D}${libdir}
    install libdisplaydebug.so libdisplaydebug.so.0.0.0
    ln -sf libdisplaydebug.so.0.0.0 libdisplaydebug.so
    ln -sf libdisplaydebug.so.0.0.0 libdisplaydebug.so.0

    install libdrmutils.so libdrmutils.so.0.0.0
    ln -sf libdrmutils.so.0.0.0 libdrmutils.so
    ln -sf libdrmutils.so.0.0.0 libdrmutils.so.0

    install libqdutils.so libqdutils.so.0.0.0
    ln -sf libqdutils.so.0.0.0 libqdutils.so
    ln -sf libqdutils.so.0.0.0 libqdutils.so.0

    install libqservice.so libqservice.so.0.0.0
    ln -sf libqservice.so.0.0.0 libqservice.so
    ln -sf libqservice.so.0.0.0 libqservice.so.0

    install libsdmcore.so libsdmcore.so.0.0.0
    ln -sf libsdmcore.so.0.0.0 libsdmcore.so
    ln -sf libsdmcore.so.0.0.0 libsdmcore.so.0

    install libsdmutils.so libsdmutils.so.0.0.0
    ln -sf libsdmutils.so.0.0.0 libsdmutils.so
    ln -sf libsdmutils.so.0.0.0 libsdmutils.so.0
}

FILES_${PN}-dev += "${libdir}/hw/gralloc.default.so"

INHIBIT_PACKAGE_STRIP="1"
INHIBIT_PACKAGE_DEBUG_SPLIT="1"
