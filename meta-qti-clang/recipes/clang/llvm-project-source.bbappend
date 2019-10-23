SRC_URI = "\
    ${BASEURI} \
    file://0001-lldb-Add-lxml2-to-linker-cmdline-of-xml-is-found.patch \
    file://0002-libcxxabi-Find-libunwind-headers-when-LIBCXXABI_LIBU.patch \
    file://0003-compiler-rt-support-a-new-embedded-linux-target.patch \
    file://0004-compiler-rt-Simplify-cross-compilation.-Don-t-use-na.patch \
    file://0005-compiler-rt-Disable-tsan-on-OE-glibc.patch \
    file://0006-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch \
    file://0007-llvm-allow-env-override-of-exe-path.patch \
    file://0008-llvm-Enhance-path-prefix-mapping.patch \
    ${@'file://0009-clang-Enable-SSP-and-PIE-by-default.patch' if '${GCCPIE}' else ''} \
    file://0010-clang-driver-Use-lib-for-ldso-on-OE.patch \
    file://0011-clang-Driver-tools.cpp-Add-lssp_nonshared-on-musl.patch \
    file://0012-clang-musl-ppc-does-not-support-128-bit-long-double.patch \
    file://0013-clang-Prepend-trailing-to-sysroot.patch \
    file://0014-clang-Look-inside-the-target-sysroot-for-compiler-ru.patch \
    file://0015-clang-Define-releative-gcc-installation-dir.patch \
    file://0016-clang-Fix-ldso-for-musl-on-x86-and-x32-architectures.patch \
    file://0017-clang-scan-view-needs-python-2.x.patch \
    file://0018-clang-Initial-implementation-of-fmacro-prefix-map-an.patch \
    file://0019-clang-Add-lpthread-and-ldl-along-with-lunwind-for-st.patch \
    file://0020-clang-default-to-lp64d-ABI-and-rv64gc-ISA.patch \
    file://0021-Driver-Prioritize-SYSROOT-usr-include-over-RESOURCE_.patch \
    file://0022-RISCV-Add-support-for-floating-point-registers-in-in.patch \
    file://0023-Pass-PYTHON_EXECUTABLE-when-cross-compiling-for-nati.patch \
    file://0024-openmp-Recognise-ARMv7ve-machine-arch.patch \
"

