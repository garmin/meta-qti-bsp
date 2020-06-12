BASEDISTRO ?= "msm"
include conf/distro/msm.conf

DISTRO_NAME = "automotive"
DISTROOVERRIDES =. "automotive:"

PRODUCT ?= "${BASEMACHINE}" 
DEPLOY_NAME_BASE = "${PRODUCT}-automotive"
IMAGE_NAME = "${IMAGE_BASENAME}-${PRODUCT}${IMAGE_VERSION_SUFFIX}"
IMAGE_LINK_NAME = "${IMAGE_BASENAME}-${PRODUCT}"

# Support systemd-user-session
DISTRO_FEATURES_append = " pam"

# Display
DISTRO_FEATURES_append = " wayland"

#Disable ssd listener
DISTRO_FEATURES_append = " no-ssd-listener-support"

# Use synergy, disable bluez
DISTRO_FEATURES_BACKFILL_CONSIDERED += "bluez5 bluetooth"

#Enable persist-image
DISTRO_FEATURES_append = " persist-image "
PERSIST_IMAGE_ROOTFS_SIZE ?= "128M"

DISTRO_FEATURES_append = " early-eth"
#DISTRO_FEATURES_append = " early_init"
#DISTRO_FEATURES_append = " early_userspace"

DISTRO_FEATURES_append = " ab-boot-support"

# Use systemd init manager.
DISTRO_FEATURES_append = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"
VIRTUAL-RUNTIME_dev_manager = "udev"
VIRTUAL-RUNTIME_init_manager = "systemd"

DISTRO_FEATURES_append = " ipa3"

IMAGE_EXT4_EXTRA_OPTIONS = "${@bb.utils.contains('DISTRO_FEATURES','ro-rootfs',' -J -b 1024 ','',d)}"

INCOMPATIBLE_LICENSE = "GPL-3.0 LGPL-3.0 AGPL-3.0"

include ${DISTRO_INC_FILES}
