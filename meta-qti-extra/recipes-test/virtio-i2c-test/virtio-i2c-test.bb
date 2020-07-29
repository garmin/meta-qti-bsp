SUMMARY = "Unit test for virtio i2c driver"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module module-sign
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/unit-test/kernel-unit-test/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/unit-test/kernel-unit-test/virtio_i2c_unit_test;subpath=virtio_i2c_unit_test;usehead=1"

SRCREV  = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/unit-test/kernel-unit-test/virtio_i2c_unit_test"
MODULES_PATH = "${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/unit_test"

do_install () {
    install -d ${D}/${base_libdir}/modules/${KERNEL_VERSION}/unit_test
    install -D -m 0644 ${S}/virtio_i2c_unit_test.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}/unit_test/
    install -D -m 0644 ${S}/virtio_i2c_unittest_kprobe.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}/unit_test/
    install -D -m 0644 ${S}/fault_injection_virtio_i2c_unit_test.ko ${D}/${base_libdir}/modules/${KERNEL_VERSION}/unit_test/
}

FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/virtio_i2c_unit_test.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/virtio_i2c_unittest_kprobe.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/fault_injection_virtio_i2c_unit_test.ko"