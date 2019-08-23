inherit autotools-brokensep

DESCRIPTION = "Safe integer operation library for C"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=f3b90e78ea0cffb20bf5cca7947a896d"

SRC_URI   =  "${PATH_TO_REPO}/external/safe-iop/.git;protocol=${PROTO};destsuffix=external/safe-iop;usehead=1"
SRC_URI_append  = " file://autotools.patch"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/external/safe-iop"

PR = "r0"
