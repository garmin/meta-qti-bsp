FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = " file://0001-DRM-front-end-display-DRM-front-end.patch"
SRCREV = "${AUTOREV}"

PACKAGECONFIG ??= "libkms radeon amdgpu nouveau vmwgfx omap freedreno vc4 etnaviv install-test-programs"
S = "${WORKDIR}/libdrm-2.4.100"
EXTRA_OEMESON_append= "${@bb.utils.contains('DISTRO_FEATURES', 'q-hypervisor', ' -Denable_drm-fe=yes', '', d)}"
