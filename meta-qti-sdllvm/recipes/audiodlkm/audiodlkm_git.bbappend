inherit sdllvm

DEPENDS += "llvm-arm-toolchain-native"

# compile with sdllvm.
KERNEL_CC = "${CC} -fuse-ld=bfd"
TOOLCHAIN = "sdllvm"
