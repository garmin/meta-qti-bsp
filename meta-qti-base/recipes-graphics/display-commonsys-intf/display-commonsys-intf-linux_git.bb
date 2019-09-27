inherit autotools qcommon

DESCRIPTION = "display commonsys intf Library"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r3"

SRC_DIR = "${SRC_DIR_ROOT}/vendor/qcom/opensource/commonsys-intf/display"
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/commonsys-intf/display/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/commonsys-intf/display;usehead=1"
S = "${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display"
SRCREV = "${AUTOREV}"

DEPENDS += "liblog libutils libcutils libhardware-headers"

EXTRA_OECONF += " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

LDFLAGS += "-llog -lutils -lcutils"

CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/gralloc"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/libqdmetadata"
CPPFLAGS += "-I${WORKDIR}/vendor/qcom/opensource/commonsys-intf/display/include"

FILES_${PN} = "${libdir}/*.so"
FILES_${PN}-dev = "${includedir}"

do_install_append() {
    install -d ${D}${includedir}
    install ${S}/gralloc/*.h ${D}${includedir}
}

INHIBIT_PACKAGE_STRIP="1"
INHIBIT_PACKAGE_DEBUG_SPLIT="1"
