inherit autotools linux-kernel-base

DESCRIPTION = "Open Source kernel tests"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://qcom-opensource/kernel/kernel-tests/"

DEPENDS = "virtual/kernel"
# This DEPENDS is to serialize kernel module builds
DEPENDS_append_mdm9635 = " qcacld-ll rtsp-alg"

PR = "r3"

S = "${WORKDIR}/qcom-opensource/kernel/kernel-tests"
CFLAGS_pn-${PN} = ""
CPPFLAGS_pn-${PN} = ""
CXXFLAGS_pn-${PN} = ""
LDFLAGS_pn-${PN} = ""
PACKAGE_STRIP = "no"
KERNEL_VERSION = "${@get_kernelversion('${STAGING_KERNEL_DIR}')}"

EXTRA_OEMAKE += "ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX}"

EXTRA_OECONF = "--prefix=/usr/kernel-tests \
                --with-kernel=${STAGING_KERNEL_DIR} \
                --disable-sps \
                --with-sanitized-headers=${STAGING_KERNEL_DIR}/usr/include"

EXTRA_OECONF_append_msm7627a = " --disable-ion"
EXTRA_OECONF_append_msm7627a = " --disable-ocmem"
EXTRA_OECONF_append_msm7627a = " --enable-v4l2apps"

FILES_${PN}-dbg = "${prefix}/kernel-tests/*/.debug/* ${prefix}/src/debug/*"
FILES_${PN}-dbg += "${libdir}/*.so ${libdir}/.debug/*"
FILES_${PN} = "${prefix}/kernel-tests/* ${prefix}/src/*"
FILES_${PN} += "${datadir}/pixmaps/* ${libdir}/*"
