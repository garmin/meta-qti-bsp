require conf/distro/poky-agl.conf
require conf/include/base-agl.inc
require conf/include/agl-appfw-smack.inc
require conf/include/agl-pulseaudio.inc

# Enable agl hmi framework
DISTRO_FEATURES_append = " agl-hmi-framework"

# reset ROOT_HOME in conf/include/agl-appfw-smack.inc
ROOT_HOME = "/home/root"

# Add buildname to fix error
USER_CLASSES = "buildstats buildname image-mklibs"

# TCLIBCAPPEND is set to empty in conf/distro/poky.conf, so $TMPDIR is $TOPDIR/tmp
# Reset the value as meta/conf/distro/defaultsetup.conf
TCLIBCAPPEND = "-${TCLIBC}"

