IMAGE_INSTALL += " \
    packagegroup-qti-core-minimal \
    packagegroup-qti-audio \
    ${@bb.utils.contains('DISTRO_FEATURES', 'synergy', 'packagegroup-qti-bluetooth', '', d)} \
    packagegroup-qti-data \
    packagegroup-qti-display \
    packagegroup-qti-extra \
    packagegroup-qti-multimedia\
    packagegroup-qti-security \
    packagegroup-qti-vnw \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wlan', 'packagegroup-qti-wlan', '', d)} \
    packagegroup-qti-tools \
    "
