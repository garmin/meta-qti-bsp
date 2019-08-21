inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

# It will remove the dynamic library related kernel moduel signing before build done and
#cause compile error if enable rm_workm hence the exclusion below.
RM_WORK_EXCLUDE += "${PN}"
