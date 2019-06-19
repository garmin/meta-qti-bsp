inherit qticlang

TOOLCHAIN = "clang"
DEPENDS += "clang-native"

FILESEXTRAPATHS_append := ":${THISDIR}/files"
SRC_URI += " file://clang.cfg "

KERNEL_CC = "${CC} -fuse-ld=bfd"

