IMAGE_INSTALL += " \
    packagegroup-qti-core-minimal \
    ${@bb.utils.contains('DISTRO_FEATURES', 'audio', 'packagegroup-qti-audio', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'synergy', 'packagegroup-qti-bluetooth', '', d)} \
    packagegroup-qti-data \
    packagegroup-qti-display \
    packagegroup-qti-extra \
    packagegroup-qti-multimedia\
    packagegroup-qti-security \
    packagegroup-qti-vnw \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wlan', 'packagegroup-qti-wlan', '', d)} \
    packagegroup-qti-tools \
    ${@bb.utils.contains('DISTRO_FEATURES', 'qdrive', 'packagegroup-qti-ros packagegroup-qti-qdrive packagegroup-qti-location-hal', '', d)} \
    "
