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

# specific steps to dm-verity image

python do_generate_verity_dts(){
    import re,os
    reDataBlocks = re.compile(r'^Data blocks:\s+(\w+)$')
    reDataBlockSize = re.compile(r'^Data block size:\s+(\w+)$')
    reHashBlockSize = re.compile(r'^Hash block size:\s+(\w+)$')
    reAlgorithm = re.compile(r'^Hash algorithm:\s+(\w+)$')
    reSalt = re.compile(r'^Salt:\s+(\w+)$')
    reHash = re.compile(r'^Root hash:\s+(\w+)$')
    DEPLOY_DIR_IMAGE = d.getVar('DEPLOY_DIR_IMAGE', True)
    ROOT_DEVICE = d.getVar('ROOT_DEVICE', True)
    fp = open('%s/dm-verity/hash_info.txt'%(DEPLOY_DIR_IMAGE))
    for text in fp.readlines():
        if(reDataBlocks.search(text)):
            dataBlocks = reDataBlocks.search(text).groups()[0]
        if(reDataBlockSize.search(text)):
            dataBlockSize = reDataBlockSize.search(text).groups()[0]
        if(reHashBlockSize.search(text)):
            hashBlockSize = reHashBlockSize.search(text).groups()[0]
        if(reAlgorithm.search(text)):
            algorithm = reAlgorithm.search(text).groups()[0]
        if(reSalt.search(text)):
            salt = reSalt.search(text).groups()[0]
        if(reHash.search(text)):
            hash = reHash.search(text).groups()[0]

    fp.close()
    fp = open('%s/dm-verity/dm-verity-boot.dtsi'%(DEPLOY_DIR_IMAGE),'w')
    fp.write('dm_verity {\n')
    fp.write('\tdmname="system";\n')
    fp.write('\tversion="1";\n')
    fp.write('\tdata_device="%s";\n'%(ROOT_DEVICE))
    fp.write('\thash_device="%s";\n'%(ROOT_DEVICE))
    fp.write('\tdata_block_size="%s";\n'%(dataBlockSize))
    fp.write('\thash_block_size="%s";\n'%(hashBlockSize))
    fp.write('\tnumber_of_data_blocks="%s";\n'%(dataBlocks))
    fp.write('\thash_start_block="%s";\n'%(int(dataBlocks)+1))
    fp.write('\talgorithm="%s";\n'%(algorithm))
    fp.write('\t// root hash: 64 bytes long\n')
    fp.write('\tdigest="%s";\n'%(hash))
    fp.write('\tsalt="%s";\n'%(salt))
    fp.write('\t// restart_on_corruption ignore_corruption ignore_zero_blocks\n')
    fp.write('\topt="restart_on_corruption";\n')
    fp.write('};\n')
    fp.close()
}

do_rebuild_dtb(){
    KERNEL_BUILD=${TMPDIR}/work-shared/${MACHINE}/kernel-build-artifacts
    KERNEL_SOURCE=${TMPDIR}/work-shared/${MACHINE}/kernel-source
    cp ${DEPLOY_DIR_IMAGE}/dm-verity/dm-verity-boot.dtsi ${KERNEL_SOURCE}/arch/arm64/boot/dts/qcom
}
