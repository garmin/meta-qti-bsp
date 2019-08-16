inherit native

DESCRIPTION = "Boot image creation tool from Android"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
HOMEPAGE = "http://android.git.kernel.org/?p=platform/system/core.git"
PROVIDES = "mkbootimg-native"

S = "${WORKDIR}/system/core/mkbootimg"
DEPENDS = "libmincrypt-native"

SRC_URI  =  "${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core/mkbootimg;subpath=mkbootimg;usehead=1"
SRC_URI_append = " file://makefile;subdir=system/core/mkbootimg"

SRCREV = "${AUTOREV}"
PR = "r6"

EXTRA_OEMAKE = "INCLUDES='-Imincrypt' LIBS='-lmincrypt'" 

do_configure[noexec]="1"
do_install() {
	install -d ${D}${bindir}
	install ${S}/mkbootimg ${D}${bindir}
}

NATIVE_INSTALL_WORKS = "1"
