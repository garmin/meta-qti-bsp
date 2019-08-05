# 1. Add files extra paths.
# 2. Update agl-audio-plugin source code to QTI version.
# 3. Update dependent pulseaudio version to 12.2.

PULSE_PV="12.2"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agl-audio-plugin/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agl-audio-plugin;usehead=1"
SRCREV  = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agl-audio-plugin/"

EXTRA_OECMAKE_append += " -DTARGET_BOARD_PLATFORM=${BASEMACHINE}"
