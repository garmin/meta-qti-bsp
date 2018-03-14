inherit deploy
DESCRIPTION = "UEFI bootloader"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=0835ade698e0bcf8506ecda2f7b4f302"

PROVIDES = "virtual/bootloader"
PV       = "3.0"
PR       = "r1"

BUILD_OS = "linux"

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILESPATH =+ "${WORKSPACE}/bootable/bootloader/:"

SRC_URI = "file://edk2"
S         =  "${WORKDIR}/edk2"

INSANE_SKIP_${PN} = "arch"

EXTRA_OEMAKE = "'ANDROID_BUILD_TOP=${WORKSPACE}'\
                'TARGET_GCC_VERSION=4.9'\
                'CLANG_BIN=${STAGING_BINDIR_NATIVE}/llvm-arm-toolchain/bin/' \
                'CLANG_PREFIX=${STAGING_BINDIR_NATIVE}/${TARGET_SYS}/${TARGET_PREFIX}' \
                'TARGET_ARCHITECTURE=MACHINE_ARCH'\
                'BUILDDIR=${S}'\
                'BOOTLOADER_OUT=${S}/out'\
                'ENABLE_LE_VARIANT=true'\
                'EDK_TOOLS_PATH=${S}/BaseTools'"

do_compile () {
    export CC=${BUILD_CC}
    export CXX=${BUILD_CXX}
    export LD=${BUILD_LD}
    export AR=${BUILD_AR}
    oe_runmake -f makefile all
}
do_install() {
        install -d ${D}/boot
}

do_configure[noexec]="1"

FILES_${PN} = "/boot"
FILES_${PN}-dbg = "/boot/.debug"

do_deploy() {
        install ${D}/boot/abl.elf ${DEPLOYDIR}
}

do_deploy[dirs] = "${S} ${DEPLOYDIR}"
addtask deploy before do_build after do_install

PACKAGE_STRIP = "no"
