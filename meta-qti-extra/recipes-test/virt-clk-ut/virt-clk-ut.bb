SUMMARY = "Unit test for virtio clock driver"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module module-sign

FILESPATH =+ "${SRC_DIR_ROOT}/vendor/qcom/opensource/unit-test/kernel-unit-test:"
SRC_URI = "file://virtio_clk"

S = "${WORKDIR}/virtio_clk"

MODULES_PATH = "${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/unit_test"

do_install () {
    install -d ${D}/lib/modules/${KERNEL_VERSION}/unit_test
    install -D -m 0644 ${S}/virtio_clk_unit_test.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
    install -D -m 0644 ${S}/kprobe_virtio_clk_ut.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
    install -D -m 0644 ${S}/fault_injection_virtio_clk_test.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
}

FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/virtio_clk_unit_test.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/kprobe_virtio_clk_ut.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/fault_injection_virtio_clk_test.ko"
