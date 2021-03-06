inherit autotools-brokensep module qperf distro_features_check

DESCRIPTION = "Qualcomm Atheros WLAN CLD3.0 low latency driver"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

FILES_${PN}     += "lib/firmware/wlan/*"
FILES_${PN}     += "lib/modules/${KERNEL_VERSION}/extra/wlan.ko"
PROVIDES_NAME   = "kernel-module-wlan"
RPROVIDES_${PN} += "${PROVIDES_NAME}-${KERNEL_VERSION}"

do_unpack[deptask] = "do_populate_sysroot"
PR = "r8"

# This DEPENDS is to serialize kernel module builds
DEPENDS = "rtsp-alg"
DEPENDS_remove_automotive = "rtsp-alg"
DEPENDS_automotive += "llvm-arm-toolchain-native"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://wlan/qcacld-3.0/"
SRC_URI += "file://wlan/qca-wifi-host-cmn/"
SRC_URI += "file://wlan/fw-api/"
SRC_URI_append_automotive = " file://device/qcom/wlan/romelv/WCNSS_qcom_cfg.ini"

S1 = "${WORKDIR}/wlan/qca-wifi-host-cmn/"
S = "${WORKDIR}/wlan/qcacld-3.0/"

FIRMWARE_PATH = "${D}/lib/firmware/wlan/qca_cld"

# Explicitly disable HL to enable LL as current WLAN driver is not having
# simultaneous support of HL and LL.
EXTRA_OEMAKE += "CONFIG_CLD_HL_SDIO_CORE=n CONFIG_CNSS_SDIO=n"

LDFLAGS_aarch64_automotive = "-O1 --hash-style=gnu --as-needed"

# The common header file, 'wlan_nlink_common.h' can be installed from other
# qcacld recipes too. To suppress the duplicate detection error, add it to
# SSTATE_DUPWHITELIST.
SSTATE_DUPWHITELIST += "${STAGING_DIR}/${MACHINE}${includedir}/qcacld/wlan_nlink_common.h"

inherit systemd
SRC_URI_append_automotive = " file://init_qti_wlan.service"
SYSTEMD_SERVICE_${PN}_automotive = "init_qti_wlan.service"
SYSTEMD_AUTO_ENABLE_${PN}_automotive = "enable"

do_compile_prepend_automotive() {
    #Add gnu99 for compiler compatible issues
    sed -i '$a\ccflags-y += -std=gnu99' ${S}/Kbuild
    #In yocto system, get build tag by 'git log' in wlan src dir instead of 'git reflog' in work dir
    sed -i -e '/^ifeq ($(CONFIG_BUILD_TAG), y)/,/^endif/{/^ifeq ($(CONFIG_BUILD_TAG), y)/!{/^endif/!d}}' ${S}/Kbuild
    sed -i -e '/^ifeq ($(CONFIG_BUILD_TAG), y)/a\
WLAN_ROOT_LV = ${WORKSPACE}/wlan/qcacld-3.0\
WLAN_CMN_LV = ${WORKSPACE}/wlan/qca-wifi-host-cmn\
CLD_IDS = $(shell cd "$(WLAN_ROOT_LV)" && git log -1 | sed -nE '\''s/^\\s*Change-Id: (I[0-f]{10})[0-f]{30}\\s*\$\$/\\1/p'\'')\
CMN_IDS = $(shell cd "$(WLAN_CMN_LV)" && git log -1 | sed -nE '\''s/^\\s*Change-Id: (I[0-f]{10})[0-f]{30}\\s*\$\$/\\1/p'\'')\
TIMESTAMP = $(shell date -u +'%Y-%m-%dT%H:%M:%SZ')\
BUILD_TAG = "$(TIMESTAMP); cld:$(CLD_IDS); cmn:$(CMN_IDS);"\
CFLAGS_wlan_hdd_main.o += -DBUILD_TAG=\\"$(BUILD_TAG)\\"' ${S}/Kbuild
}


do_install () {
    module_do_install

    install -d ${FIRMWARE_PATH}
    install -d ${D}${includedir}/qcacld/
    install -m 0644 ${S1}/utils/nlink/inc/wlan_nlink_common.h ${D}${includedir}/qcacld/

    #copying wlan.ko to STAGING_DIR_TARGET
    WLAN_KO=${@oe.utils.conditional('PERF_BUILD', '1', '${STAGING_DIR_TARGET}-perf', '${STAGING_DIR_TARGET}', d)}
    install -d ${WLAN_KO}/wlan
    install -m 0644 ${S}/wlan.ko ${WLAN_KO}/wlan/
}

do_install_append_automotive() {
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/romelv/WCNSS_qcom_cfg.ini ${FIRMWARE_PATH}
    # Install systemd service file
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0644 ${WORKDIR}/init_qti_wlan.service -D ${D}${systemd_unitdir}/system/init_qti_wlan.service
    fi
}

do_module_signing() {
    if [ -f ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ]; then
        bbnote "Signing ${PN} module"
        ${STAGING_KERNEL_DIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ${STAGING_KERNEL_BUILDDIR}/signing_key.x509 ${PKGDEST}/${PROVIDES_NAME}/lib/modules/${KERNEL_VERSION}/extra/wlan.ko
    elif [ -f ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ]; then
        ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${PKGDEST}/${PN}/lib/modules/${KERNEL_VERSION}/extra/wlan.ko
    else
        bbnote "${PN} module is not being signed"
    fi
}

addtask module_signing after do_package before do_package_write_ipk

do_compile_automotive() {
    if [ -e Makefile -o -e makefile -o -e GNUmakefile ]; then
        qoe_runmake CROSS_COMPILE=${CROSS_COMPILE} ${KERNEL_EXTRA_ARGS}|| die "make failed"
    else
        bbnote "nothing to compile"
    fi
}

qoe_runmake() {
    qoe_runmake_call "$@" || die "oe_runmake failed"
}

qoe_runmake_call() {
    bbnote make ${EXTRA_OEMAKE} CC="${STAGING_BINDIR_NATIVE}/llvm-arm-toolchain/bin/clang" "$@"
    make ${EXTRA_OEMAKE} CC="${STAGING_BINDIR_NATIVE}/llvm-arm-toolchain/bin/clang" "$@"
}

REQUIRED_DISTRO_FEATURES = "systemd"
