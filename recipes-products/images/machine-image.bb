# Get list of packages to be installed onto the root filesystem.
# If product is specified try to include product inc otherwise include base inc.
def get_img_inc_file(d):
    product     = d.getVar('PRODUCT', True)
    basemachine = d.getVar('BASEMACHINE', True)
    if product != 'base' or '':
        inc_file_name = basemachine + "-" + product + "-image.inc"
    else:
        inc_file_name = basemachine + "-" + "base-image.inc"
    img_inc_file_path = os.path.join(d.getVar('THISDIR'), basemachine, inc_file_name)
    if os.path.exists(img_inc_file_path):
        img_inc_file = inc_file_name
    else:
        img_inc_file = basemachine + "-base-image.inc"
    return img_inc_file

include ${BASEMACHINE}/${@get_img_inc_file(d)}

require include/mdm-bootimg.inc

require include/mdm-ota-target-image-ubi.inc
require include/mdm-ota-target-image-ext4.inc

inherit core-image

MULTILIBRE_ALLOW_REP =. "/usr/include/python2.7/*|${base_bindir}|${base_sbindir}|${bindir}|${sbindir}|${libexecdir}|${sysconfdir}|${nonarch_base_libdir}/udev|/lib/modules/[^/]*/modules.*|"

do_fsconfig() {
 chmod go-r ${IMAGE_ROOTFS}/etc/passwd || :
 chmod -R o-rwx ${IMAGE_ROOTFS}/etc/init.d/ || :
 if [ "${DISTRO_NAME}" == "msm-user" ]; then
  if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
    rm ${IMAGE_ROOTFS}/lib/systemd/system/sys-kernel-debug.mount
  else
    sed -i '/mount -t debugfs/ d' ${IMAGE_ROOTFS}/etc/init.d/sysfs.sh
  fi
 fi
}

ROOTFS_POSTPROCESS_COMMAND += "do_fsconfig; "
