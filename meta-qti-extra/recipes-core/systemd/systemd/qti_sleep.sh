#!/bin/sh
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

case $1/$2 in
  pre/*)
    echo "Entering into $2..."

    # set all usb mode to none
    echo none > /sys/devices/platform/soc/a400000.ssusb/mode
    echo none > /sys/devices/platform/soc/a600000.ssusb/mode
    echo none > /sys/devices/platform/soc/a800000.ssusb/mode

    systemctl stop audiod.service
    if [ $2 == "hibernate" ]; then
        echo 0 > /sys/kernel/boot_adsp/boot
    fi

    # disable BT as hsuart could block suspend
    systemctl stop synergy.service

    # WA: wait for ack from cdsp/adsp on DIAG USB disconnect event.
    # It's a wakeup source IRQ which would break suspend.
    sleep 0.2
    ;;
  post/*)
    echo "Exiting from $2..."

    systemctl restart synergy.service

    if [ $2 == "hibernate" ]; then
        echo 1 > /sys/kernel/boot_adsp/boot
    fi
    systemctl restart audiod.service

    # restore all usb mode
    echo host > /sys/devices/platform/soc/a400000.ssusb/mode
    echo peripheral > /sys/devices/platform/soc/a600000.ssusb/mode
    echo host > /sys/devices/platform/soc/a800000.ssusb/mode
    ;;
esac
