FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append += "file://0001-libnaviapi-agl-add-multilib-support.patch"

EXTRA_OECMAKE += "-DLIB_SUFFIX=${@d.getVar('baselib', True).replace('lib', '')}"

