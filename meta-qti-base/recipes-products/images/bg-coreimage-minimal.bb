SUMMARY = "A small image for BG."

IMAGE_INSTALL = "packagegroup-core-boot ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = ""

inherit core-image

DEPENDS += "edk2"

CORE_IMAGE_EXTRA_INSTALL += "\
    audiodlkm \
    openssh-scp \
    openssh-ssh \
    openssh-sshd \
    openssh-sftp \
    openssh-sftp-server \
    weston \
    media \
    "

IMAGE_FEATURES += " \
    debug-tweaks \
    ssh-server-openssh \
"
