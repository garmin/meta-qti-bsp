#! /bin/sh
# Copyright (c) 2020, The Linux Foundation. All rights reserved.

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

LXC_MOUNT_POINT="/lxc"
LXC_DEV="/dev/disk/by-partlabel/lxc"
OVERLAY_DIRS="usr etc lib var/lib"

if [ ! -d $LXC_MOUNT_POINT ] || [ ! -L $LXC_DEV ]; then
	echo "lxc-init: no mount point or device"
	exit 1
fi

e2fsck -fy $LXC_DEV > /dev/null 2>&1
mount -t ext4 $LXC_DEV $LXC_MOUNT_POINT > /dev/null 2>&1
if [ $? != 0 ] ; then
	echo "lxc-init: mount failed"
	exit 1
fi

# install lxc delta files by overlayfs
for DIR in $OVERLAY_DIRS
do
	WORK_DIR="$LXC_MOUNT_POINT/.work/$DIR"
	mkdir -p $WORK_DIR
	mount -t overlay overlay -olowerdir="/$DIR",upperdir="$LXC_MOUNT_POINT/$DIR",workdir="$WORK_DIR" "/$DIR"
done

# cleanup on lxc host environment
systemctl mask weston.service

exit 0
