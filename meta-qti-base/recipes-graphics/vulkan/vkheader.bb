SUMMARY = "Vulkan header files and API registry"
DESCRIPTION = "Vulkan is a new generation graphics and compute API that \
provides high-efficiency, cross-platform access to modern GPUs used in a\
wide variety of devices from PCs and consoles to mobile phones and embedded \
platforms"

HOMEPAGE = "https://www.khronos.org/"
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3b83ef96387f14655fc854ddc3c6bd57"

PN = 'vkheader'
PV = '1'

inherit cmake pkgconfig

SRCREV = "v1.1.126"
SRC_URI = "git://source.codeaurora.org/quic/le/external/khronosgroup/vulkan-headers;nobranch=1;protocol=https"

S = "${WORKDIR}/git"

FILES_${PN}-dev = "${includedir}/*  /usr/share/vulkan/*"