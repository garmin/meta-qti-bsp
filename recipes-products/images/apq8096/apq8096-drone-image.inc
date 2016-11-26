#Include the drone settings

# Get base product settings
require ${MACHINE}-base-image.inc

#IMAGE_INSTALL += "q6-admin-adsp"
IMAGE_INSTALL += "q6-admin-slpi"
IMAGE_INSTALL += "lib32-live555-qti"
IMAGE_INSTALL += "lib32-media"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "av-frameworks"
IMAGE_INSTALL += "sigma-dut"
IMAGE_INSTALL += "qcacld-utils"
IMAGE_INSTALL += "data"
IMAGE_INSTALL += "wlan-services"
IMAGE_INSTALL += "libcap"
IMAGE_INSTALL += "attr"
IMAGE_INSTALL += "iputils"

IMAGE_INSTALL += "lib32-libbt-vendor"
IMAGE_INSTALL += "lib32-fluoride"
IMAGE_INSTALL += "lib32-bt-property"
IMAGE_INSTALL += "lib32-bt-app"