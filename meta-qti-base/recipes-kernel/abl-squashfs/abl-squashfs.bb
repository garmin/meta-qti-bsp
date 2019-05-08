require recipes-kernel/edk2/edk2_git.bb
SRC_URI += "file://modify-rootfs-type-for-squashfs.patch"

PROVIDES_remove = "virtual/bootloader"


do_deploy() {
        install ${D}/boot/abl.elf ${DEPLOYDIR}/abl-squashfs.elf
}
