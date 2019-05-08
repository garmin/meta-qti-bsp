
# The debug build variant requires packages with symbol information for ease
# of on target debugging.
INHIBIT_PACKAGE_STRIP = "${@bb.utils.contains('DEBUG_BUILD', '1', '1', '', d)}"
INHIBIT_PACKAGE_DEBUG_SPLIT = "${@bb.utils.contains('DEBUG_BUILD', '1', '1', '', d)}"
PACKAGE_DEBUG_SPLIT_STYLE="${@bb.utils.contains('DEBUG_BUILD', '1', '.debug', '', d)}"

# binutils are required for analysis of binaries
IMAGE_INSTALL += "${@bb.utils.contains('DEBUG_BUILD', '1', 'binutils', '', d)}"
