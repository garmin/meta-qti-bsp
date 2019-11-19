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
mkdir -p /var/lib/lxc/android/rootfs
mkdir -p /var/lib/lxc/android/rootfs/vendor
mount /dev/disk/by-partlabel/system_b /var/lib/lxc/android/rootfs
mount /dev/disk/by-partlabel/vendor_a /var/lib/lxc/android/rootfs/vendor

echo "sharing the firmware/bluetooth/dsp partiton with host"
mount --bind /firmware /var/lib/lxc/android/rootfs/vendor/firmware_mnt
mount --bind /bluetooth /var/lib/lxc/android/rootfs/vendor/bt_firmware
mount --bind /dsp /var/lib/lxc/android/rootfs/vendor/dsp
mkdir -p /var/lib/lxc/android/rootfs/mnt/vendor/persist
mkdir -p /var/lib/lxc/android/persist
mount --bind /var/lib/lxc/android/persist /var/lib/lxc/android/rootfs/mnt/vendor/persist
mkdir -p /data/android/data/
mount --bind /data/android/data/ /var/lib/lxc/android/rootfs/data

echo "mounting prepare persist/dsp/firmware/bt_firmware"

# disable usb
FILE=/var/lib/lxc/android/rootfs/vendor/etc/init/hw/init.qcom.usb.rc
FILE_BAK=/var/lib/lxc/android/rootfs/vendor/etc/init/hw/init.qcom.usb.rc.bak
if [ -f "$FILE" ]; then
  mv $FILE $FILE_BAK
fi

# remove mounting
sed -i '/system/d' /var/lib/lxc/android/rootfs/vendor/etc/fstab.qcom
sed -i '/userdata/d' /var/lib/lxc/android/rootfs/vendor/etc/fstab.qcom
sed -i '/persist/d' /var/lib/lxc/android/rootfs/vendor/etc/fstab.qcom
sed -i '/dsp/d' /var/lib/lxc/android/rootfs/vendor/etc/fstab.qcom
sed -i '/vfat/d' /var/lib/lxc/android/rootfs/vendor/etc/fstab.qcom

echo "prepare rootfs for android done"
