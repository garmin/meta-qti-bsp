inherit qlicense qcommon
DESCRIPTION = "Provide native media hardware Headers"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;usehead=1"
S = "${WORKDIR}/frameworks"
SRCREV = "${AUTOREV}"

do_install() {
    install -d ${D}${includedir}/media/hardware
    install -m 0644 ${S}/native/include/media/hardware/*.h -D ${D}${includedir}/media/hardware/
}

ALLOW_EMPTY_${PN} = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

