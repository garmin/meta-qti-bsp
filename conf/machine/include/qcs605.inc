BASEMACHINE ?= "qcs605"
require conf/machine/include/basemachine.inc

SOC_FAMILY ?= "sdm670"
include conf/machine/include/soc-family.inc

require conf/machine/include/arm/arch-armv8.inc

BBMASK = ".*/*/recipes-kernel/linux-msm-4.4"
BBMASK .= "|.*/*/recipes-kernel/linux-quic"

SERIAL_CONSOLE = "115200 ttyMSM0"

DISTRO ?= "msm"

DISTRO_FEATURES += "persist-image"

PAGE_SIZE ?= '4096'

# Max supported inodes
EXTRA_IMAGECMD ?= "-N 2048"

# Sizes for EXT4 (in bytes)
SYSTEM_SIZE_EXT4 ?= "3000000000"
SYSTEMRW_SIZE_EXT4 ?= "8388608"
USERDATA_SIZE_EXT4 ?= "5000000000"
CACHE_SIZE_EXT4 ?= "65536000"

# Enable LE Verified Boot
DISTRO_FEATURES_append = " vble"

#Enable SDK feature
#SDK_IMAGE ?= "1"

#Enable Recovery image generation
RECOVERY_IMAGE ?= "1"

# Formats of root filesystem images.
IMAGE_FSTYPES += "ext4"
