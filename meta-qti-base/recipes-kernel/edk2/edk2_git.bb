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

SRC_URI   =  "${PATH_TO_REPO}/bootable/bootloader/edk2/.git;protocol=${PROTO};destsuffix=edk2;nobranch=1"
S         =  "${WORKDIR}/edk2"
SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/bootable/bootloader/edk2', d)}"

INSANE_SKIP_${PN} = "arch"

VBLE = "${@bb.utils.contains('DISTRO_FEATURES', 'vble','1', '0', d)}"

VERITY_ENABLED = "${@bb.utils.contains('DISTRO_FEATURES', 'dm-verity','1', '0', d)}"

EARLY_ETH = "${@bb.utils.contains('DISTRO_FEATURES', 'early-eth', '1', '0', d)}"

EXTRA_OEMAKE = "'CLANG_BIN=${STAGING_BINDIR_NATIVE}/llvm-arm-toolchain/bin/' \
                'CLANG_PREFIX=${STAGING_BINDIR_NATIVE}/${TARGET_SYS}/${TARGET_PREFIX}' \
                'TARGET_ARCHITECTURE=${TARGET_ARCH}'\
                'BUILDDIR=${S}'\
                'BOOTLOADER_OUT=${S}/out'\
                'ENABLE_LE_VARIANT=true'\
                'VERIFIED_BOOT_LE=${VBLE}'\
                'VERITY_LE=${VERITY_ENABLED}'\
                'INIT_BIN_LE=\"/sbin/init\"'\
                'EDK_TOOLS_PATH=${S}/BaseTools'\
                'EARLY_ETH_ENABLED=${EARLY_ETH}'"

EXTRA_OEMAKE_append_qcs40x = " 'DISABLE_PARALLEL_DOWNLOAD_FLASH=1'"

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


python sstate_task_prefunc () {
    import os
    shared_state = sstate_state_fromvars(d)
    ssmanifest = "%s/manifest-%s-%s.deploy" % (d.getVar("SSTATE_MANIFESTS"), d.getVar("SSTATE_MANMACH") , d.getVar("PN"))
    if (shared_state['task'] == "deploy") and (os.path.exists(ssmanifest)):
        cmd = "echo '%s/abl.elf' > %s" % (d.getVar("DEPLOY_DIR_IMAGE"), ssmanifest)
        os.system(cmd)
    sstate_clean(shared_state, d)
}
