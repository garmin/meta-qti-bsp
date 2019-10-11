# Add supprot for kernel unit test framework
RDEPENDS_${PN} += "\
    ${@bb.utils.contains('DISTRO_FEATURES', 'ktf', 'ktf', '', d)} \
    "
