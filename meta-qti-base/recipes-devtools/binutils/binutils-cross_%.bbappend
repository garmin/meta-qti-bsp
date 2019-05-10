require binutils-2.29.1.inc


SRC_URI_remove += "file://0002-binutils-cross-Do-not-generate-linker-script-directo.patch"

SRC_URI += "https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-devtools/binutils/binutils/0002-binutils-cross-Do-not-generate-linker-script-directo.patch?h=yocto/rocko;downloadfilename=0002-binutils-cross-Do-not-generate-linker-script-directo.patch;md5sum=c9e3adeca916e738236fdb7c1b0a2d48;sha256sum=11b12671b193b9f2b0301160d135a94d719b831ac0e2c130ba54b39cf9f7e77c"
