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
#

python __anonymous(){
    import re
#    bb.warn("SRC_URI=%s******" % d.getVar('SRC_URI'))
    d.prependVar("FILESPATH", "${SRC_DIR_ROOT}/:")
    if (d.getVar('SRC_URI')) != "" :
        src_uri = ((d.getVar('SRC_URI')).strip() or '').split(' ')
        new_uri = ""
        num = 0

        pattern = re.compile(r'protocol=file')
        first_url = src_uri[0]
        for url in src_uri:
            if pattern.search(url):
                num = num + 1

        if pattern.search(first_url):
            file_path = re.findall(r"destsuffix=(.+?);", first_url)
#            bb.note("file_path=%s******" % file_path)
            if file_path != []:   
                src_uri[0] = "file://" + file_path[0]

        for p in src_uri:
            new_uri = new_uri + ' ' + p

        if num == 1:
            d.setVar("SRCREV", '')
 
        d.setVar("SRC_URI", new_uri)   
}


