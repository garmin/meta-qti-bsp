SUMMARY = "Vulkan header files and API registry"
DESCRIPTION = "Vulkan is a new generation graphics and compute API that \
provides high-efficiency, cross-platform access to modern GPUs used in a\
wide variety of devices from PCs and consoles to mobile phones and embedded \
platforms"

HOMEPAGE = "https://www.khronos.org/"
SECTION = "libs"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=7dbefed23242760aa3475ee42801c5ac"

PN = 'vkloader'
PV = '1'
DEPENDS = "vkheader wayland"

inherit cmake pkgconfig

SRCREV = "v1.1.126"
SRC_URI = "git://source.codeaurora.org/quic/le/external/khronosgroup/vulkan-loader;nobranch=1;protocol=https"

S = "${WORKDIR}/git"

EXTRA_OECMAKE = "\
	-DBUILD_WSI_XCB_SUPPORT:BOOL=OFF \
	-DBUILD_WSI_XLIB_SUPPORT=OFF \
	"

FILES_${PN} = " ${libdir}/libvulkan.so*"
FILES_${PN}-dev = " ${libdir}/libvulkan.so* ${libdir}/pkgconfig/*.pc"