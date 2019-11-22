#Fetch the package from github to reduce license risk. This bbappend file can be masked.
SRC_URI_remove = "http://ftp.openbsd.org/pub/OpenBSD/OpenSSH/portable/openssh-${PV}.tar.gz"
SRC_URI_prepend = "https://github.com/openssh/openssh-portable/archive/V_8_0_P1.tar.gz " 

SRC_URI[md5sum] = "8030c9b19014c0b70e3413f10207e63b"
SRC_URI[sha256sum] = "b3e045eb199ae26e89289ea69a6ee69d6c1645a44c5316feb22ebe79e0929d9f"

S = "${WORKDIR}/openssh-portable-V_8_0_P1"

