DEPENDS +="${@bb.utils.contains('DISTRO_FEATURES', 'le-clang','llvm-arm-toolchain-native','',d)}"
FULL_OPTIMIZATION = "${@bb.utils.contains('DISTRO_FEATURES', 'le-clang',\
  '-O2  -fomit-frame-pointer -Wno-error=maybe-uninitialized  -Wno-error=unused-result -Wno-error=unknown-warning-option -Wno-error=unused-comparison \
    -Wno-error=unused-private-field -Wno-error=undefined-optimized -Wno-error=format -Wno-error=inconsistent-missing-override', \
  '-O2 -fexpensive-optimizations -frename-registers -fomit-frame-pointer -ftree-vectorize   -Wno-error=maybe-uninitialized -finline-functions -finline-limit=64', d)}"

NEON_FLAGS = "${@bb.utils.contains('TARGET_ARCH', 'arm', '-march=armv7-a -mfloat-abi=softfp -mfpu=neon -ftree-vectorize ', ' ',d)}"

CLANG_CPP   = "${@bb.utils.contains_any('TARGET_ARCH', 'arm aarch64', 'CPP="${STAGING_BINDIR_TOOLCHAIN}/../llvm-arm-toolchain/bin/clang -E  \
              -target ${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS} --sysroot=${STAGING_DIR_TARGET} ${NEON_FLAGS}" ', ' ',d)}"
CLANG_CC   = "${@bb.utils.contains_any('TARGET_ARCH', 'arm aarch64', 'CC="${STAGING_BINDIR_TOOLCHAIN}/../llvm-arm-toolchain/bin/clang  \
              -target ${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS} --sysroot=${STAGING_DIR_TARGET} ${NEON_FLAGS}" ', ' ',d)}"

CLANG_CXX  =  "${@bb.utils.contains_any('TARGET_ARCH', 'arm aarch64', 'CXX="${STAGING_BINDIR_TOOLCHAIN}/../llvm-arm-toolchain/bin/clang++  \
              -target ${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS} --sysroot=${STAGING_DIR_TARGET} ${NEON_FLAGS}" ', ' ',d)}"
EXTRA_OECONF +="${@bb.utils.contains('DISTRO_FEATURES', 'le-clang','${CLANG_CPP}', ' ',d)}"

EXTRA_OECONF +="${@bb.utils.contains('DISTRO_FEATURES', 'le-clang','${CLANG_CC}', ' ',d)}"

EXTRA_OECONF +="${@bb.utils.contains('DISTRO_FEATURES', 'le-clang','${CLANG_CXX}', ' ',d)}"
