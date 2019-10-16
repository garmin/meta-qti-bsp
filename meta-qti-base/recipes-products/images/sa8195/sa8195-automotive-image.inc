IMAGE_INSTALL += " \
    packagegroup-qti-core-minimal \
    packagegroup-qti-data \
    packagegroup-qti-display \
    packagegroup-qti-extra \
    packagegroup-qti-multimedia\
    packagegroup-qti-security \
    packagegroup-qti-vnw \
    ${@bb.utils.contains('DISTRO_FEATURES', 'audio', 'packagegroup-qti-audio', '', d)} \
    packagegroup-qti-tools \
    "
