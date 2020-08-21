SUMMARY = "Kernel Test Framework Unit test for khab driver"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

inherit module module-sign
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/unit-test/kernel-unit-test/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/unit-test/kernel-unit-test/khab_test;subpath=khab_test;usehead=1"

SRCREV  = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/unit-test/kernel-unit-test/khab_test"
MODULES_PATH = "${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/unit_test"
do_install () {
	install -d ${D}/lib/modules/${KERNEL_VERSION}/unit_test
	install -D -m 0644 ${S}/fault_injection_khab_test_export.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/fault_injection_khab_test_import.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/fault_injection_khab_test_open.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/fault_injection_khab_test_unexport.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/fault_injection_khab_test_unimport.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_close.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_export.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_import.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_open.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_query.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_recv.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_send.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_unexport.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_test_unimport.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
	install -D -m 0644 ${S}/khab_unittest_kprobe.ko ${D}/lib/modules/${KERNEL_VERSION}/unit_test/
}

FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/fault_injection_khab_test_export.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/fault_injection_khab_test_import.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/fault_injection_khab_test_open.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/fault_injection_khab_test_unexport.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/fault_injection_khab_test_unimport.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_close.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_export.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_import.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_open.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_query.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_recv.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_send.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_unexport.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_test_unimport.ko"
FILES_${PN} += "${base_libdir}/modules/${KERNEL_VERSION}/unit_test/khab_unittest_kprobe.ko"
