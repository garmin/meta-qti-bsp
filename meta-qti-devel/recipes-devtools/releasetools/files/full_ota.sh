#!/bin/sh
# Copyright (c) 2017, The Linux Foundation. All rights reserved.
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
#
# full_ota.sh      script to generate OTA upgrade pacakges.
#

set -o xtrace

if [ "$#" -lt 3 ]; then
    echo "Usage  : $0 target_files_zipfile rootfs_path ext4_or_ubi [-c fsconfig_file [-p prefix]]"
    echo "------------------------------------------------------------------"
    echo "example: $0 target_files_ubi.zip  machine_image/1.0-r0/rootfs ubi"
    echo "example: $0 target_files_ext4.zip machine_image/1.0-r0/rootfs ext4"
    echo "example: $0 target_files_ext4.zip machine_image/1.0-r0/rootfs ext4  -p system/ -c fsconfig.conf --block"
    exit 1
fi

USR_LIB=../recipe-sysroot-native/usr/lib
LIB=../recipe-sysroot-native/lib
USR_BIN=../recipe-sysroot-native/usr/bin

export PATH=.:${USR_LIB}:${LIB}:${USR_BIN}:$PATH:/usr/bin
export OUT_HOST_ROOT=.

export LD_LIBRARY_PATH=${USR_LIB}:${LIB}:${USR_BIN}
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export LANGUAGE=en_US.UTF-8
export FSCONFIGFOPTS=" "
block_based=" "
python_version="python3"

if [ "$#" -gt 3 ]; then
    IFS=' ' read -a allopts <<< "$@"
    i=4
    for i in $(seq 3 $#); do
       if [ "${allopts[${i}]}" = "--block" ]; then
           block_based="${allopts[${i}]}"
       else
           FSCONFIGFOPTS=$FSCONFIGFOPTS${allopts[${i}]}" "
       fi
    done
fi

# Specify MMC or MTD type device. MTD by default
[[ $3 = "ext4" ]] && device_type="MMC" || device_type="MTD"

target_files=target_files_$3
rm -rf $target_files
unzip -qo $1 -d $target_files
mkdir -p $target_files/META
mkdir -p $target_files/SYSTEM
mkdir -p target_files/BOOT/RAMDISK
touch target_files/BOOT/RAMDISK/empty

if [ "${block_based}" = "--block" ]; then
    # python2 is needed for block based OTA.
    python_version="python2"
else
    # File-based OTA needs this to assign the correct context to '/' after OTA upgrade
    echo "/system -d system_u:object_r:root_t:s0" >> $target_files/BOOT/RAMDISK/file_contexts

    # File-based OTA also can use canned_fs_config to set uid, gid & mode to all the files
    # The "-x" is to tell fs_config that the modes in fsconfig file are in hex, not octal.
    if [ -e $target_files/META/system.canned.fsconfig ]; then
        FSCONFIGFOPTS=${FSCONFIGFOPTS}" -p system/ -x $target_files/META/system.canned.fsconfig "
    fi
fi

# Generate selabel rules only if file_contexts is packed in target-files
if grep "selinux_fc" $target_files/META/misc_info.txt
then
    zipinfo -1 $1 |  awk 'BEGIN { FS="SYSTEM/" } /^SYSTEM\// {print "system/" $2}' | fs_config ${FSCONFIGFOPTS} -C -S $target_files/BOOT/RAMDISK/file_contexts -D ${2} > $target_files/META/filesystem_config.txt
else
    zipinfo -1 $1 |  awk 'BEGIN { FS="SYSTEM/" } /^SYSTEM\// {print "system/" $2}' | fs_config ${FSCONFIGFOPTS} -D ${2} > $target_files/META/filesystem_config.txt
fi

cd $target_files && zip -q ../$1 META/*filesystem_config.txt SYSTEM/build.prop BOOT/RAMDISK/empty && cd ..


$python_version ./ota_from_target_files $block_based -n -v -d $device_type -p . -m linux_embedded --no_signing  $1 update_$3.zip > ota_debug.txt 2>&1

if [[ $? = 0 ]]; then
    echo "update.zip generation was successful"
else
    echo "update.zip generation failed"
    # Add the python script errors back into the target-files zip
    zip -q $1 ota_debug.txt
    rm update_$3.zip # delete the half-baked update.zip if any;
fi
