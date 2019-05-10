require binutils-2.29.1.inc


SRC_URI_remove += "file://0001-binutils-crosssdk-Generate-relocatable-SDKs.patch"

SRC_URI += "https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-devtools/binutils/binutils/0001-binutils-crosssdk-Generate-relocatable-SDKs.patch?h=yocto/rocko;downloadfilename=0001-binutils-crosssdk-Generate-relocatable-SDKs.patch;md5sum=89b98fc422dd9b5ca90dc0c59f2bc2bc;sha256sum=61dfec74638bd6cc50a33571c2eb63c645291d489d76fd6bcde3e2ecb1028fdc"