SUMMARY = "A very basic Wayland image with a terminal"

require recipes-products/images/qti-image-minimal.bb
require recipes-products/images/qti-image-weston-prop.bb

IMAGE_INSTALL_append = "\
    packagegroup-qti-display \
    "
REQUIRED_DISTRO_FEATURES = "wayland"
