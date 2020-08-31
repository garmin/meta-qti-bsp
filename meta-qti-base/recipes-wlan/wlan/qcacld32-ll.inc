inherit autotools-brokensep module qperf module-sign distro_features_check systemd

PROVIDES_NAME   = "kernel-module-${_MODNAME}"
RPROVIDES_${PN} += "${PROVIDES_NAME}-${KERNEL_VERSION}"

do_unpack[deptask] = "do_populate_sysroot"

FILES_${PN}     += "lib/firmware/wlan/*"
FILES_${PN}     += "lib/firmware/*"
FILES_${PN}     += "lib/modules/${KERNEL_VERSION}/extra/${_MODNAME}.ko"
FILES_${PN}     += "usr/bin/init.qti.wlan_on.sh"
FILES_${PN}     += "usr/bin/init.qti.wlan_off.sh"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"

SYSTEMD_SERVICE_${PN} = "init_qti_wlan_auto.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_compile() {
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
    bbnote make ${EXTRA_OEMAKE} CC="${CLANG_BIN_PATH}/clang" "$@"
    make ${EXTRA_OEMAKE} CC="${CLANG_BIN_PATH}/clang" "$@"
}

REQUIRED_DISTRO_FEATURES = "systemd"
