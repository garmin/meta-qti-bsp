#############################################################################
#@COMMENT: This distribution configuration defines an OE Linux based
#@COMMENT: distribution. The matching buildable image target (machine-image)
#@COMMENT: basically consists of: libc, busybox, udev, sysv init, and a few
#@COMMENT: init scripts for running up the system on qti chipsets.
#############################################################################
BASEDISTRO ??= ""

# Add BASEDISTRO to overrides
DISTROOVERRIDES =. "${@['', '${BASEDISTRO}:']['${BASEDISTRO}' != '']}"

# DISTRO FEATURE SELECTION
MICRO_GOLD ?= "ld-is-gold"
MICRO_GOLD_apq8017 = ""

USE_DEVFS = "0"

# Use Debian naming scheme for library (.so) files
INHERIT += "recipe_sanity"

# TOOLCHAIN
PREFERRED_VERSION_autoconf = "2.68"
ARM_INSTRUCTION_SET       ?= "arm"

FULL_OPTIMIZATION          = "-O2 -Wa,--noexecstack -fexpensive-optimizations -frename-registers -fomit-frame-pointer -ftree-vectorize "
FULL_OPTIMIZATION         += " -finline-functions -finline-limit=64 "
# Ignore a few common warnings
FULL_OPTIMIZATION         += " -Wno-error=maybe-uninitialized -Wno-error=unused-result "

# NLS
USE_NLS = "no"
USE_NLS_glib-2.0 = "yes"
USE_NLS_glib-2.0-native = "yes"
USE_NLS_gcc-cross = "no"

# Disable binary locale generation
ENABLE_BINARY_LOCALE_GENERATION = "0"

#Allow library symlinks to exist alongside soname files
PACKAGE_SNAP_LIB_SYMLINKS = "0"

# Don't install ldconfig and associated gubbins
USE_LDCONFIG = "0"
LDCONFIGDEPEND = ""
COMMERCIAL_LICENSE_DEPENDEES = ""

PREFERRED_VERSION_readline = "5.2"

PRODUCT ??= "base"

# Retain existing directory structure for msm or mdm distro images.
# Append variants like '-perf', '-user' etc to deploy dir.
DEPLOY_NAME_BASE = "${MACHINE}${@bb.utils.contains_any('DISTRO', 'msm mdm', '', '-' + d.getVar('DISTRO'), d)}"
DEPLOY_NAME = "${DEPLOY_NAME_BASE}${@['-' + d.getVar('VARIANT', True), ''][d.getVar('VARIANT', True) == ('' or 'debug')]}"
DEPLOY_DIR_IMAGE = "${DEPLOY_DIR}/images/${DEPLOY_NAME}"

INHERIT += "extrausers"

EXTRA_USERS_PARAMS = "\
 useradd -u 1000 -U system; \
 useradd -u 1001 -U radio; \
 useradd -u 1002 -U bluetooth; \
 useradd -u 1003 -U graphics; \
 useradd -u 1004 -g input input; \
 useradd -u 1005 -g audio audio; \
 useradd -u 1006 -U camera;\
 useradd -u 1010 -U wifi; \
 useradd -u 1013 -U media; \
 useradd -u 1021 -U gps; \
 useradd -u 1036 -U logd;\
 useradd -u 2000 -U adb; \
 useradd -u 3011 -U sensors; \
 useradd -u 4000 -U apps; \
 useradd -u 4021 -U locclient; \
 useradd -u 9999 -U nobody; \
 useradd -u 5000 -U qmmfsvr; \
 useradd -u 5001 -U qmmfwebsvr; \
 useradd -u 5002 -U leprop; \
 usermod -L -e 1 system; \
 usermod -L -e 1 radio; \
 usermod -L -e 1 bluetooth; \
 usermod -L -e 1 graphics; \
 usermod -L -e 1 input; \
 usermod -L -e 1 audio; \
 usermod -L -e 1 camera; \
 usermod -L -e 1 wifi; \
 usermod -L -e 1 media; \
 usermod -L -e 1 adb; \
 usermod -L -e 1 sensors; \
 usermod -L -e 1 apps; \
 usermod -L -e 1 adb; \
 usermod -L -e 1 sensors; \
 usermod -L -e 1 Apps; \
 usermod -L -e 1 nobody; \
 usermod -L -e 1 qmmfsvr; \
 usermod -L -e 1 qmmfwebsvr; \
 usermod -L -e 1 leprop; \
 usermod -L -e 1 logd; \
 usermod -a -G system,inet,leprop qmmfwebsvr; \
 usermod -a -G system,leprop qmmfsvr; \
 usermod -a -G diag,inet,system,leprop sensors; \
 usermod -a -G inet,system,leprop camera; \
 usermod -a -G leprop bluetooth; \
 usermod -a -G system,gps,locclient gps; \
 groupadd -g 3005 net_admin; \
 groupadd -g 3009 readproc; \
 groupadd -g 3010 wakelock; \
"

EXTRA_USERS_PARAMS_append = " ${@['', 'usermod -L -e 1 root;'][d.getVar('VARIANT', True) == 'user']}"
