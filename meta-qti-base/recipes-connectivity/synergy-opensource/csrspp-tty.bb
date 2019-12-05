DESCRIPTION = "QTI Synergy opensource SA8155 AGL Platform"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "BSD-3-Clause & GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module module-sign

DEPENDS = "virtual/kernel"

_MODNAME = "csrspp-tty"
FILES_${PN}     += "lib/modules/${KERNEL_VERSION}/extra/${_MODNAME}.ko"
PROVIDES_NAME   = "kernel-module-${_MODNAME}"
RPROVIDES_${PN} += "${PROVIDES_NAME}-${KERNEL_VERSION}"

SRC_URI = "${PATH_TO_REPO}/synergy/synergy-opensource/.git;protocol=${PROTO};destsuffix=synergy/synergy-opensource;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/synergy/synergy-opensource/platform/msm/spp"
