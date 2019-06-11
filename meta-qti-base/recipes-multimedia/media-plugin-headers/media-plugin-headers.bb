inherit qlicense qcommon
DESCRIPTION = "Provide native media hardware Headers"

SRC_URI = "${PATH_TO_REPO}/frameworks/.git;protocol=${PROTO};destsuffix=frameworks;nobranch=1"
S = "${WORKDIR}/frameworks"
SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/frameworks', d)}"

do_install() {
    install -d ${D}${includedir}/media/hardware
    install -m 0644 ${S}/native/include/media/hardware/*.h -D ${D}${includedir}/media/hardware/
}

ALLOW_EMPTY_${PN} = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

