
# change the SRC dir
SRC_URI = "${PATH_TO_REPO}/external/dtc/.git;protocol=${PROTO};destsuffix=external/dtc;nobranch=1"
S = "${WORKDIR}/external/dtc"
EXTRA_OEMAKE_append = " NO_PYTHON=1"

SRCREV = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/external/dtc', d)}"

FILESEXTRAPATHS_append := ":${THISDIR}/files"
SRC_URI_append = " file://0001-dtc-fix-Android-dtc-compile-issue.patch"

do_fetch_prepend(){
    bb.warn('NOTE: bbappend overrides dtc recipe\'s version and sets it to Android 1.4.4')
}

