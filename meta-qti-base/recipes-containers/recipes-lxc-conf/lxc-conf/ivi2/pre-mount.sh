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

echo "prepare rootfs for ivi2 using overlay..."

IVI2_ROOTFS=/var/lib/lxc/ivi2/rootfs

mkdir -p /data/ivi2/rootfs_upper
mkdir -p /data/ivi2/rootfs_workdir
mkdir -p ${IVI2_ROOTFS}
mount -t overlay -o lowerdir=/,upperdir=/data/ivi2/rootfs_upper,workdir=/data/ivi2/rootfs_workdir overlay ${IVI2_ROOTFS}

# remove fstab
sed -i '/^PARTLABEL/d' ${IVI2_ROOTFS}/etc/fstab
sed -i '/^\/data/d' ${IVI2_ROOTFS}/etc/fstab
sed -i '/^\/var/d' ${IVI2_ROOTFS}/etc/fstab

# set hostname ivi2
echo "LV-infotainment" > ${IVI2_ROOTFS}/etc/hostname

#enable weston in ivi2
rm ${IVI2_ROOTFS}/etc/systemd/system/weston.service
rm ${IVI2_ROOTFS}/lib/systemd/system/multi-user.target.wants/usb.service
rm ${IVI2_ROOTFS}/lib/systemd/system/multi-user.target.wants/adbd.service

#disable adsprpcd
rm ${IVI2_ROOTFS}/lib/systemd/system/adsprpcd*.service

#disable connman
FILE=${IVI2_ROOTFS}/lib/systemd/system/connman.service
FILE_BAK=${IVI2_ROOTFS}/lib/systemd/system/connman.service.bak
if [ -f "$FILE" ]; then
    mv $FILE $FILE_BAK
fi
if [ -L ${IVI2_ROOTFS}/etc/resolv.conf ]; then
    rm ${IVI2_ROOTFS}/etc/resolv.conf
fi
echo "nameserver 8.8.8.8" > ${IVI2_ROOTFS}/etc/resolv.conf

# use systemd-networkd for route config
cp /var/lib/lxc/ivi2/lxc-wired.network ${IVI2_ROOTFS}/etc/systemd/network/

mkdir -p ${IVI2_ROOTFS}/home/root

echo "prepare rootfs for ivi2 done"
