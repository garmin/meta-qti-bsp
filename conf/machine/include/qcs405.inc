BASEMACHINE ?= "qcs405"
require conf/machine/include/basemachine.inc

#To BE CHANGED ONCE VT IS READY
SOC_FAMILY ?= "mdm9x07"
include conf/machine/include/soc-family.inc

# Pull in security flags
require ${COREBASE}/meta-qti-bsp/conf/distro/include/security_flags.inc

include conf/machine/include/arm/arch-armv8.inc

# TODO: preferred kernel will be linux-msm_4.14"
PREFERRED_PROVIDER_virtual/kernel = "linux-msm"
PREFERRED_VERSION_linux-msm = "4.9"

MACHINE_DISPLAY_WIDTH_PIXELS ?= "480"
MACHINE_DISPLAY_HEIGHT_PIXELS ?= "640"

SERIAL_CONSOLE ?= "${@['', '115200 ttyHSL0'][d.getVar('VARIANT', True) == ('' or 'debug')]}"

DISTRO ?= "msm"

DISTRO_FEATURES_append = " ro-rootfs"

PAGE_SIZE ?= '4096'

MACHINE_FEATURES += "kernel26 keyboard"

# TODO: ADD qcs405 entries, once kernel is ready.
KERNEL_DTS_NAME = "sdxpoorwills"
KERNEL_BASE = "0x80000000"
KERNEL_TAGS_OFFSET = "0x82000000"
KERNEL_DEFCONFIG = "sdxpoorwills_defconfig"
KERNEL_PERF_DEFCONFIG = "sdxpoorwills-perf_defconfig"

KERNEL_CMD_PARAMS = "noinitrd rw rootwait console=ttyMSM0,115200,n8 no_console_suspend=1 androidboot.hardware=qcom androidboot.console=ttyMSM0 lpm_levels.sleep_disabled=1"
