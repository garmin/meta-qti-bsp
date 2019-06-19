DESCRIPTION = "The minimal set of packages required to boot the system, and some baics tools"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-minimal \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    packagegroup-qti-core-boot \
    packagegroup-qti-core-commonlibs \
    "

RDEPENDS_${PN} += "\
    kernel-modules \
    system-core-adbd \
    system-core-leprop \
    system-core-logd \
    system-core-post-boot \
    system-core-usb \
    "
