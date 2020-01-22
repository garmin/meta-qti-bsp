#! /bin/sh

# Copyright (c) 2019, The Linux Foundation. All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#     * Redistributions of source code must retain the above copyright
#       notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
#       copyright notice, this list of conditions and the following
#       disclaimer in the documentation and/or other materials provided
#       with the distribution.
#     * Neither the name of The Linux Foundation nor the names of its
#       contributors may be used to endorse or promote products derived
#       from this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
# ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
# BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
# BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
# OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
# IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

echo "prepare rootfs for android"

ANDROID_ROOTFS=/var/lib/lxc/android/rootfs
mkdir -p ${ANDROID_ROOTFS}

echo "prepare mounting"
if [ -h "/dev/disk/by-partlabel/la_userdata" ]; then
    mount /dev/disk/by-partlabel/la_system /var/lib/lxc/android/rootfs
    mount /dev/disk/by-partlabel/la_vendor /var/lib/lxc/android/rootfs/vendor
    mount /dev/disk/by-partlabel/la_userdata /var/lib/lxc/android/rootfs/data
    mount /dev/disk/by-partlabel/la_persist /var/lib/lxc/android/rootfs/mnt/vendor/persist
else
    # For backward compatibility, will be removed later
    mount /dev/disk/by-partlabel/system_b ${ANDROID_ROOTFS}
    mount /dev/disk/by-partlabel/vendor_a ${ANDROID_ROOTFS}/vendor
    mkdir -p /var/lib/lxc/android/persist
    mount --bind /var/lib/lxc/android/persist ${ANDROID_ROOTFS}/mnt/vendor/persist
    mkdir -p /data/android/data/
    mount --bind /data/android/data/ ${ANDROID_ROOTFS}/data
fi

echo "sharing the firmware/bluetooth/dsp partiton with host"
mount --bind /firmware ${ANDROID_ROOTFS}/vendor/firmware_mnt
mount --bind /bluetooth ${ANDROID_ROOTFS}/vendor/bt_firmware
mount --bind /dsp ${ANDROID_ROOTFS}/vendor/dsp

# disable usb
FILE=${ANDROID_ROOTFS}/vendor/etc/init/hw/init.qcom.usb.rc
FILE_BAK=${ANDROID_ROOTFS}/vendor/etc/init/hw/init.qcom.usb.rc.bak
if [ -f "$FILE" ]; then
  mv $FILE $FILE_BAK
fi

# disable adsprpcd/cdsp
FILE=${ANDROID_ROOTFS}/vendor/etc/init/vendor.qti.adsprpc-service.rc
if [ -f "$FILE" ]; then
   rm $FILE
fi

# change android console
echo "ro.boot.console=tty7" >> ${ANDROID_ROOTFS}/default.prop

sed -i '/vendor.adsprpcd/,+3d'  ${ANDROID_ROOTFS}/vendor/etc/init/hw/init.target.rc
sed -i '/vendor.cdsprpcd/,+3d'  ${ANDROID_ROOTFS}/vendor/etc/init/hw/init.target.rc
sed -i '/boot_adsp/d'  ${ANDROID_ROOTFS}/vendor/etc/init/hw/init.qcom.rc
sed -i '/boot_cdsp/d'  ${ANDROID_ROOTFS}/vendor/etc/init/hw/init.qcom.rc

#FIXME
sed -i 's/chmod\ 0660\ \/dev\/ttyHS0/chmod\ 0666\ \/dev\/ttyHS0/g' ${ANDROID_ROOTFS}/vendor/etc/init/hw/init.qcom.rc

# remove mounting
sed -i '/system/d' ${ANDROID_ROOTFS}/vendor/etc/fstab.qcom
sed -i '/userdata/d' ${ANDROID_ROOTFS}/vendor/etc/fstab.qcom
sed -i '/persist/d' ${ANDROID_ROOTFS}/vendor/etc/fstab.qcom
sed -i '/dsp/d' ${ANDROID_ROOTFS}/vendor/etc/fstab.qcom
sed -i '/vfat/d' ${ANDROID_ROOTFS}/vendor/etc/fstab.qcom

echo "prepare rootfs for android done"
