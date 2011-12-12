# Force a few additional dependencies in the mix so that we get the needed
# recipes to build in the right order so we can make the bootimg file.
DEPENDS = 	" \
		virtual/kernel \
		virtual/bootloader \
		pkgconfig-native \
		gtk-doc-native \
		gettext-native \
		mkbootimg-native \
		"



# Make the bootimg image file using the information available in the sysroot...
do_build_append() {

	ver=`sed -r 's/#define UTS_RELEASE "(.*)"/\1/' ${STAGING_DIR_TARGET}/usr/src/linux/include/generated/utsrelease.h`
	kernelsize=`awk --non-decimal-data '/ _end/ {end="0x" $1} /_stext/ {beg="0x" $1} END {size1=end-beg+4096; size=and(size1,compl(4095)); printf("%#x",size)}' ${STAGING_DIR_TARGET}/boot/System.map-${ver}`

	${STAGING_DIR_NATIVE}/bin/mkbootimg --kernel ${STAGING_DIR_TARGET}/boot/Image-${ver} \
		--ramdisk /dev/null \
		--cmdline "noinitrd root=/dev/mtdblock8 rw rootfstype=yaffs2 console=ttyHSL0,115200,n8 no_console_suspend=1 androidboot.hardware=qcom" \
		--base 0x40800000 \
		--ramdisk_offset $kernelsize \
		--output ${DEPLOY_DIR_IMAGE}/boot-oe-msm9615.img

}