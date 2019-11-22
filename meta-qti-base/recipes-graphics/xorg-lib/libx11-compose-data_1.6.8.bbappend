#Fetch the package from github to reduce license risk. This bbappend file can be masked.
SRC_URI = "https://github.com/mirror/libX11/archive/libX11-1.6.8.tar.gz"
SRC_URI += "file://0001-Drop-x11-dependencies.patch"
S = "${WORKDIR}/${XORG_PN}-${XORG_PN}-${PV}"

SRC_URI[md5sum] = "a26e36879b1e817ebbbb674bad90cc8f"
SRC_URI[sha256sum] = "2afc1f1a0cbcdb111372b0712580cf3b55b518afc10751ec95eb5f6bc3e5755d"

