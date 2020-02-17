#This common include file contains modules common
#to the mctm and AGL project. The project specific
#modules needs to added in specific machine related
#{MACHINE}-dm-verity-image.inc file

LICENSE = "GPLv2"

DEPENDS = "cryptsetup-native"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_install[noexec] = "1"
do_package[noexec] = "1"
do_packagedata[noexec] = "1"
do_package_write_ipk[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_populate_lic[noexec] = "1"
do_package_qa[noexec] = "1"
do_qa_configure[noexec] = "1"

include ${BASEMACHINE}/${BASEMACHINE}-dm-verity-image.inc

do_make_system_image () {
  # generate verity image
  mkdir -p ${DEPLOY_DIR_IMAGE}/dm-verity
  dd if=/dev/zero of=${DEPLOY_DIR_IMAGE}/dm-verity/hashtable.img bs=1M count=1
  #export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:${TMPDIR}/sysroots/${BUILD_SYS}/lib:${TMPDIR}/sysroots/${BUILD_SYS}/usr/lib
  if [ -f ${DEPLOY_DIR_IMAGE}/machine-image-${PRODUCT}.ext4 ]; then
    veritysetup format ${DEPLOY_DIR_IMAGE}/machine-image-${PRODUCT}.ext4 ${DEPLOY_DIR_IMAGE}/dm-verity/hashtable.img > ${DEPLOY_DIR_IMAGE}/dm-verity/hash_info.txt
    touch ${DEPLOY_DIR_IMAGE}/dm-verity/verity.conf
    cat ${DEPLOY_DIR_IMAGE}/machine-image-${PRODUCT}.ext4 ${DEPLOY_DIR_IMAGE}/dm-verity/hashtable.img > ${DEPLOY_DIR_IMAGE}/dm-verity/machine-image-${PRODUCT}.ext4
  else
    veritysetup format ${DEPLOY_DIR_IMAGE}/bg-coreimage-minimal-${PRODUCT}.ext4 ${DEPLOY_DIR_IMAGE}/dm-verity/hashtable.img > ${DEPLOY_DIR_IMAGE}/dm-verity/hash_info.txt
    touch ${DEPLOY_DIR_IMAGE}/dm-verity/verity.conf
    cat ${DEPLOY_DIR_IMAGE}/bg-coreimage-minimal-${PRODUCT}.ext4  ${DEPLOY_DIR_IMAGE}/dm-verity/hashtable.img > ${DEPLOY_DIR_IMAGE}/dm-verity/bg-coreimage-minimal-${PRODUCT}.ext4
  fi
}

python do_make_dm_verity_image(){
    if(d.getVar('KERNEL_ROOTDEVICE', True) == "/dev/dm-0"):
        bb.build.exec_func('do_make_system_image',d)
        bb.build.exec_func('do_generate_verity_conf',d)
        bb.build.exec_func('do_rebuild_verity_cmdline',d)
}

addtask do_make_dm_verity_image after do_populate_sysroot before do_build 
