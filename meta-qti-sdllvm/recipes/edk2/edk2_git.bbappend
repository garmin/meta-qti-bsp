
DEPENDS += "llvm-arm-toolchain-native"

CLANG_BIN_PATH = "${STAGING_BINDIR_NATIVE}/llvm-arm-toolchain/bin/"

EXTRA_OEMAKE += "'UBSAN_UEFI_GCC_FLAG_ALIGNMENT=-Wno-misleading-indentation'"
