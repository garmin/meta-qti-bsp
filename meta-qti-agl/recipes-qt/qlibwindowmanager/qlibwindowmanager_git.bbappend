FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append += "file://0001-qlibwindowmanager-add-multilib-support.patch"

EXTRA_QMAKEVARS_PRE += "LIB_SUFFIX=${@d.getVar('baselib', True).replace('lib', '')}"

