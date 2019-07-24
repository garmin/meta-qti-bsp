#!/bin/sh
# Copyright (c) 2019, The Linux Foundation. All rights reserved.

# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#    * Redistributions of source code must retain the above copyright
#      notice, this list of conditions and the following disclaimer.
#    * Redistributions in binary form must reproduce the above
#      copyright notice, this list of conditions and the following
#      disclaimer in the documentation and/or other materials provided
#      with the distribution.
#    * Neither the name of The Linux Foundation nor the names of its
#      contributors may be used to endorse or promote products derived
#      from this software without specific prior written permission.

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

cd /sys/devices/system/memory/
echo 0xc0000000 > probe
echo online > memory6/state
echo 0xe0000000 > probe
echo online > memory7/state
echo 0x100000000 > probe
echo online > memory8/state
echo 0x120000000 > probe
echo online > memory9/state
echo 0x140000000 > probe
echo online > memory10/state
echo 0x160000000 > probe
echo online > memory11/state
echo 0x180000000 > probe
echo online > memory12/state
echo 0x1a0000000 > probe
echo online > memory13/state
echo 0x1c0000000 > probe
echo online > memory14/state
echo 0x1e0000000 > probe
echo online > memory15/state
echo 0x200000000 > probe
echo online > memory16/state
echo 0x220000000 > probe
echo online > memory17/state
echo 0x240000000 > probe
echo online > memory18/state
