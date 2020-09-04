IMAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-core', 'packagegroup-qti-core-minimal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-audio', 'packagegroup-qti-audio', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-bluetooth', 'packagegroup-qti-bluetooth', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-data', 'packagegroup-qti-data', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-display', 'packagegroup-qti-display', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-extra', 'packagegroup-qti-extra', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-multimedia', 'packagegroup-qti-multimedia', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-security', 'packagegroup-qti-security', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-vnw', 'packagegroup-qti-vnw', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-wlan', 'packagegroup-qti-wlan', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-tools', 'packagegroup-qti-tools', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-ros', 'packagegroup-qti-ros', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-qdrive', 'packagegroup-qti-qdrive', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-location', 'packagegroup-qti-location-hal', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'kdump-support', 'kexec-tools makedumpfile linux-msm-capture', '', d)} \
    "
