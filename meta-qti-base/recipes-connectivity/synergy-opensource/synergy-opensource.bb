DESCRIPTION = "QTI Synergy opensource SA8155 AGL Platform"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS = "cmake cmake-native dbus synergy pulseaudio"

SRC_URI = "${PATH_TO_REPO}/synergy/synergy-opensource/.git;protocol=${PROTO};destsuffix=synergy/synergy-opensource;usehead=1"

SRCREV = "${AUTOREV}"

# Remove -Wl,--hash-style=gnu from it to avoid qa error for the prebuilt lib
LDFLAGS = "-Wl,-O1"

S = "${WORKDIR}/synergy"

# compile synergy-opensource
do_compile_opensource () {
    export CC="${CC}"
    export CROSS_COMPILE=${TARGET_PREFIX}

# Build synergy-opensource for qca
if [ -f "${B}/synergy-opensource/platform/msm/makefile" ]; then
    make -C ${B} -f ${B}/synergy-opensource/platform/msm/makefile all IMGRFS=${STAGING_DIR_HOST} CROSS_COMPILE=${TARGET_PREFIX} V=1 CC="${CC}" CHIP_TYPE="QCA"
fi
}


# Install synergy-opensource
do_install_opensource () {
    install -d ${D}${bindir}

# [TODO] Install bt_audio_service which can support HF and A2DP audio. Currently, HF audio is verified through "bt_hf_audio.sh".
if [ -f "${B}/synergy-opensource/platform/msm/bt_audio_service/output/bin/bt_audio_service" ]; then
    install ${B}/synergy-opensource/platform/msm/bt_audio_service/output/bin/bt_audio_service ${D}${bindir}
fi
}

#####################################################################################################

do_compile () {

    do_compile_opensource
}

do_install () {

    do_install_opensource
}

FILES_${PN} += "usr/bin \
                etc/dbus-1/system.d/"
