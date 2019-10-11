FILESEXTRAPATHS_prepend := "${THISDIR}/file:"

SRC_URI_append = " file://0001-Fix-the-variable-GST_FD_MEMORY_FLAG_DONT_CLOSE-error.patch"

# add plugins.
PACKAGECONFIG_append = "\
    v4l2 \
    audiotestsrc \
    videotestsrc \
"
