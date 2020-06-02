SUMMARY = "HOGL Logging"
DESCRIPTION = ""
HOMEPAGE = "https://github.com/maxk-org/hogl"
LICENSE = "BSD-2-Clause"

LIC_FILES_CHKSUM = "file://COPYING;md5=41eb136c1374ae06b70ae866d31b14f6"

SRCREV = "v3.0-rc1"
SRC_URI = "git://github.com/maxk-org/hogl.git;protocol=git"
SRC_URI += "file://0001-Fix-compilation-issues-from-hogl-module.patch"

inherit pkgconfig autotools
EXTRA_OECONF += " --without-tools --without-tests"
S = "${WORKDIR}/git"

