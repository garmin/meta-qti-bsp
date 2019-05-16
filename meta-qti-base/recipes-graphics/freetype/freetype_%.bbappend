PV = "2.8"

LIC_FILES_CHKSUM = "file://docs/LICENSE.TXT;md5=4af6221506f202774ef74f64932878a1 \
                    file://docs/FTL.TXT;md5=13b25413274c9b3b09b63e4028216ff4 \
                    file://docs/GPLv2.TXT;md5=8ef380476f642c20ebf40fecb0add2ec"

UPSTREAM_CHECK_REGEX = "(?P<pver>\d+(\.\d+)+)"

SRC_URI[md5sum] = "2413ac3eaf508ada019c63959ea81a92"
SRC_URI[sha256sum] = "a3c603ed84c3c2495f9c9331fe6bba3bb0ee65e06ec331e0a0fb52158291b40b"

BINCONFIG = "${bindir}/freetype-config"

inherit autotools pkgconfig binconfig-disabled multilib_header

#Apply the patch for successful compilation.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://0001-FreeType-Insert-tag-CC-into-CC.patch"

