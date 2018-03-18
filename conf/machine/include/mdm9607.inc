BASEMACHINE ?= "mdm9607"
require conf/machine/include/basemachine.inc

SOC_FAMILY ?= "mdm9x07"
include conf/machine/include/soc-family.inc

#TCLIBC = "musl"

# Pull in security flags
require ${COREBASE}/meta-qti-bsp/conf/distro/include/security_flags.inc

require conf/machine/include/tune-cortexa8.inc

PREFERRED_PROVIDER_virtual/kernel = "linux-msm"
PREFERRED_VERSION_linux-msm = "4.9%"

BBMASK = ".*/*/recipes-devtools/mtd/"
#BBMASK .= "|.*/*/recipes-security/libselinux/"

MACHINE_DISPLAY_WIDTH_PIXELS ?= "480"
MACHINE_DISPLAY_HEIGHT_PIXELS ?= "640"

SERIAL_CONSOLE ?= "${@['', '115200 ttyMSM0'][d.getVar('VARIANT', True) == ('' or 'debug')]}"

DISTRO ?= "mdm"

MACHINE_FEATURES += "kernel26 keyboard"

PAGE_SIZE ?= '4096'

# Max supported inodes
EXTRA_IMAGECMD ?= "-N 2048"

#UBINIZE Args
UBINIZE_ARGS ?= "-m 4096 -p 256KiB -s 4096"
#MKUBIFS Arg
MKUBIFS_ARGS ?= "-m 4096 -e 253952 -c 2146 -F"
#Volume sizes
SYSTEM_VOLUME_SIZE ?= "74MiB"
CACHE_VOLUME_SIZE ?= "37MiB"
SYSTEMRW_VOLUME_SIZE ?= "6MiB"

# Formats of root filesystem images.
IMAGE_FSTYPES += "ubi"
