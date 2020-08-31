SRC_URI = "\
    ${BASEURI} \
    file://0001-lldb-Add-lxml2-to-linker-cmdline-of-xml-is-found.patch \
    file://0002-libcxxabi-Find-libunwind-headers-when-LIBCXXABI_LIBU.patch \
    file://0003-compiler-rt-support-a-new-embedded-linux-target.patch \
    file://0004-compiler-rt-Simplify-cross-compilation.-Don-t-use-na.patch \
    file://0005-compiler-rt-Disable-tsan-on-OE-glibc.patch \
    file://0006-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch \
    file://0007-llvm-allow-env-override-of-exe-path.patch \
    file://0008-clang-driver-Check-sysroot-for-ldso-path.patch \
    file://0009-clang-Driver-tools.cpp-Add-lssp_nonshared-on-musl.patch \
    file://0010-clang-musl-ppc-does-not-support-128-bit-long-double.patch \
    file://0011-clang-Prepend-trailing-to-sysroot.patch \
    file://0012-clang-Look-inside-the-target-sysroot-for-compiler-ru.patch \
    file://0013-clang-Define-releative-gcc-installation-dir.patch \
    file://0014-clang-Fix-ldso-for-musl-on-x86-and-x32-architectures.patch \
    file://0015-clang-scan-view-needs-python-2.x.patch \
    file://0016-clang-Add-lpthread-and-ldl-along-with-lunwind-for-st.patch \
    file://0017-libclang-Use-CMAKE_DL_LIBS-for-deducing-libdl.patch \
    file://0018-Pass-PYTHON_EXECUTABLE-when-cross-compiling-for-nati.patch \
    file://0019-Check-for-atomic-double-intrinsics.patch \
    ${@'file://0020-clang-Enable-SSP-and-PIE-by-default.patch' if '${GCCPIE}' else ''} \
    file://0021-libcxx-Add-compiler-runtime-library-to-link-step-for.patch \
    file://0022-clang-llvm-cmake-Fix-configure-for-packages-using-fi.patch \
    file://0023-clang-Fix-resource-dir-location-for-cross-toolchains.patch \
    file://0024-fix-path-to-libffi.patch \
"

