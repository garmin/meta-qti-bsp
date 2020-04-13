inherit qticlang

TOOLCHAIN = "clang"
DEPENDS += "clang-native"

KERNEL_CC = "${CC} -fuse-ld=bfd"

