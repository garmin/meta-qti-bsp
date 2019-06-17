# Apply the patch to fix errors:
# libtool: compile: unable to infer tagged configuration
# libtool: compile: specify a tag with `--tag'
#
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://0001-FreeType-Insert-tag-CC-into-CC.patch"
