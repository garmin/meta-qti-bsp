#EXTRAPATHS_append := ":${THISDIR}/makedumpfile"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-makedumpfile-integration-for-kdump-on-gen3_auto.patch"
