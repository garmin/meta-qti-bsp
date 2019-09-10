include ${BASEMACHINE}/${BASEMACHINE}-image.inc

# Add libgomp support
IMAGE_INSTALL += "libgomp libgomp-dev libgomp-staticdev"
# Add kernel header to SDK.
TOOLCHAIN_TARGET_TASK_append = " kernel-devsrc"
