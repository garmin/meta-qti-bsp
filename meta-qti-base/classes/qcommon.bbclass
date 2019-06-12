#
# Common bitbake recipe information for QTI meta layers.
# Below are common values, statements and functions.
#
inherit autotools-brokensep pkgconfig

FILESPATH        =+ "${SRC_DIR_ROOT}:"

SRC_URI          = "file://${@d.getVar('SRC_DIR', True).replace('${SRC_DIR_ROOT}/', '')}"

PACKAGE_ARCH    ?= "${MACHINE_ARCH}"
