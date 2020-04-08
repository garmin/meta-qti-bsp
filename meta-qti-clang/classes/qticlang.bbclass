inherit clang

CC_toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CXX_toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang++ -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CPP_toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} -E ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CCLD_toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
