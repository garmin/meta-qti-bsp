inherit autotools

DESCRIPTION = "OEM recovery"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
HOMEPAGE = "https://source.codeaurora.org/quic/la/platform/vendor/qcom-opensource/recovery-ext/"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}/:"

SRC_URI = "git://source.codeaurora.org/quic/la/platform/vendor/qcom-opensource/recovery-ext;branch=qcom-devices.lnx.4.0;protocol=https"
SRC_URI_append = " file://0001-Enable-gpt-utils-for-LV.patch \
                   file://0002-Add-autotools-support.patch "

SRCREV = "1622ca7dee2a6ab1d45fe4e6e7f0a0f52f3c017a"

S = "${WORKDIR}/git"

DEPENDS += "virtual/kernel liblog libcutils glib-2.0"

EXTRA_OECONF = "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

LDFLAGS += " -lcutils -lglib-2.0"

CPPFLAGS += " -I${STAGING_INCDIR}/cutils \
              -I${STAGING_INCDIR}/glib-2.0/ \
              -I${STAGING_LIBDIR}/glib-2.0/include \
              -Dstrlcpy=g_strlcpy \
              -include glib.h "
