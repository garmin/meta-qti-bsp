# 1. Add files extra paths.
# 2. Update agl-audio-plugin source code to QTI version.
# 3. Update dependent pulseaudio version to 12.2.

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agl-audio-plugin/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agl-audio-plugin;nobranch=1"
SRCREV  = "${@base_get_metadata_git_revision('${SRC_DIR_ROOT}/vendor/qcom/opensource/agl-audio-plugin', d)}"

S = "${WORKDIR}/vendor/qcom/opensource/agl-audio-plugin/"

PULSE_PV="12.2"
