include ${BASEMACHINE}/${BASEMACHINE}-image.inc

# Add libgomp support
IMAGE_INSTALL += "libgomp libgomp-dev libgomp-staticdev"
