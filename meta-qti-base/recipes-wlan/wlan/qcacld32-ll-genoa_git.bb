inherit autotools-brokensep module qperf module-sign distro_features_check

DESCRIPTION = "Qualcomm Atheros WLAN CLD3.0 low latency driver"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

_MODNAME = "qca6595"
FW_PATH_NAME = "qcn7605"
FILES_${PN}     += "lib/firmware/wlan/*"
FILES_${PN}     += "lib/firmware/*"
FILES_${PN}     += "lib/modules/${KERNEL_VERSION}/extra/${_MODNAME}.ko"
PROVIDES_NAME   = "kernel-module-${_MODNAME}"
RPROVIDES_${PN} += "${PROVIDES_NAME}-${KERNEL_VERSION}"

do_unpack[deptask] = "do_populate_sysroot"
PR = "r8"

# This DEPENDS is to serialize kernel module builds
DEPENDS = "rtsp-alg"
DEPENDS_remove_automotive = "rtsp-alg"
DEPENDS_automotive += "llvm-arm-toolchain-native"

SRC_URI = "${PATH_TO_REPO}/wlan/qcacld-3.0/.git;protocol=${PROTO};destsuffix=wlan/qcacld-3.0;usehead=1 \
           ${PATH_TO_REPO}/wlan/qca-wifi-host-cmn/.git;protocol=${PROTO};destsuffix=wlan/qca-wifi-host-cmn;usehead=1 \
           ${PATH_TO_REPO}/wlan/fw-api/.git;protocol=${PROTO};destsuffix=wlan/fw-api/;usehead=1 \
           "

SRC_URI_append_automotive = " ${PATH_TO_REPO}/device/qcom/wlan/.git;protocol=${PROTO};destsuffix=device/qcom/wlan/msm_auto;subpath=msm_auto;usehead=1"

SRCREV = "${AUTOREV}"
SRCREV_FORMAT = "qcacld_cmn_fw_msm"

S1 = "${WORKDIR}/wlan/qca-wifi-host-cmn/"
S = "${WORKDIR}/wlan/qcacld-3.0/"

FIRMWARE_PATH = "${D}/lib/firmware/wlan/qca_cld/${_MODNAME}"

# Explicitly disable HL to enable LL as current WLAN driver is not having
# simultaneous support of HL and LL.
EXTRA_OEMAKE += "CONFIG_CLD_HL_SDIO_CORE=n CONFIG_CNSS_SDIO=n"
EXTRA_OEMAKE += "CONFIG_QCA_CLD_WLAN_PROFILE=genoa.pci.debug"
EXTRA_OEMAKE += "DYNAMIC_SINGLE_CHIP=${_MODNAME}"
EXTRA_OEMAKE += "MODNAME=${_MODNAME}"
EXTRA_OEMAKE_append_sa6155 = " WLAN_CFG_OVERRIDE="CONFIG_IPA_DISABLE_OVERRIDE=y""

LDFLAGS_aarch64_automotive = "-O1 --hash-style=gnu --as-needed"

# The common header file, 'wlan_nlink_common.h' can be installed from other
# qcacld recipes too. To suppress the duplicate detection error, add it to
# SSTATE_DUPWHITELIST.
SSTATE_DUPWHITELIST += "${STAGING_DIR}/${BASEMACHINE}${includedir}/qcacld/wlan_nlink_common.h"

inherit systemd
SRC_URI_append_automotive = " file://init_qti_wlan_auto.service"
SYSTEMD_SERVICE_${PN}_automotive = "init_qti_wlan_auto.service"
SYSTEMD_AUTO_ENABLE_${PN}_automotive = "enable"

SRC_URI_append_automotive = " file://init.qti.wlan_on.sh"
SRC_URI_append_automotive = " file://init.qti.wlan_off.sh"
FILES_${PN}     += "usr/bin/init.qti.wlan_on.sh"
FILES_${PN}     += "usr/bin/init.qti.wlan_off.sh"

do_compile_prepend_automotive() {
    #Add gnu99 for compiler compatible issues
    sed -i '$a\ccflags-y += -std=gnu99' ${S}/Kbuild
    #In yocto system, get build tag by 'git log' in wlan src dir instead of 'git reflog' in work dir
    sed -i -e '/^ifeq ($(CONFIG_BUILD_TAG), y)/,/^endif/{/^ifeq ($(CONFIG_BUILD_TAG), y)/!{/^endif/!d}}' ${S}/Kbuild
    sed -i -e '/^ifeq ($(CONFIG_BUILD_TAG), y)/a\
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
    install -m 0644 ${S}/${_MODNAME}.ko ${WLAN_KO}/wlan/
}
do_install_append_automotive() {
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/WCNSS_qcom_cfg_qcn7605.ini ${FIRMWARE_PATH}/WCNSS_qcom_cfg.ini
    install -D -m 0644 ${WORKDIR}/device/qcom/wlan/msm_auto/wlan_mac.bin ${FIRMWARE_PATH}/wlan_mac.bin
    install -d ${D}${bindir}
    install -D -m 0755 ${WORKDIR}/init.qti.wlan_on.sh ${D}${bindir}/init.qti.wlan_on.sh
    install -D -m 0755 ${WORKDIR}/init.qti.wlan_off.sh ${D}${bindir}/init.qti.wlan_off.sh

    install -d ${D}/lib/firmware/${FW_PATH_NAME}/
    ln -sf /firmware/image/${FW_PATH_NAME}/amss.bin ${D}/lib/firmware/${FW_PATH_NAME}/

    #For GNA04.1 boardid = 0xff
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan03.b01 ${D}/lib/firmware/${FW_PATH_NAME}/
    mv ${D}/lib/firmware/${FW_PATH_NAME}/bdwlan03.b01 ${D}/lib/firmware/${FW_PATH_NAME}/bdwlan.bin
    #For GNA04.1 boardid = 0x301
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan03.b01 ${D}/lib/firmware/${FW_PATH_NAME}/
    mv ${D}/lib/firmware/${FW_PATH_NAME}/bdwlan03.b01 ${D}/lib/firmware/${FW_PATH_NAME}/bdwlan.b0301
    #For GNA04.1 boardid = 0x302
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan02.b03 ${D}/lib/firmware/${FW_PATH_NAME}/
    mv ${D}/lib/firmware/${FW_PATH_NAME}/bdwlan02.b03 ${D}/lib/firmware/${FW_PATH_NAME}/bdwlan03.b02
    #For GNA04.1 boardid = 0x203
    ln -sf /firmware/image/${FW_PATH_NAME}/bdwlan02.b03 ${D}/lib/firmware/${FW_PATH_NAME}/

    # Install systemd service file
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -m 0644 ${WORKDIR}/init_qti_wlan_auto.service -D ${D}${systemd_unitdir}/system/init_qti_wlan_auto.service
    fi
}

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
