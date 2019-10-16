IMAGE_INSTALL += " \
    packagegroup-qti-core-minimal \
    ${@bb.utils.contains('DISTRO_FEATURES', 'audio', 'packagegroup-qti-audio', '', d)} \
    packagegroup-qti-bluetooth \
    packagegroup-qti-data \
    packagegroup-qti-display \
    packagegroup-qti-extra \
    packagegroup-qti-multimedia\
    packagegroup-qti-security \
    packagegroup-qti-vnw \
    "
