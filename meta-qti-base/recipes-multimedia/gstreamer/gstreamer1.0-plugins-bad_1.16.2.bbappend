DEFAULT_PREFERENCE = "-1"

FILESEXTRAPATHS_append := ":${THISDIR}/gst-plugins-bad"
SRC_URI += "file://0001-gst-plugins-bad-implement-share-buffer-mode-for-gst.patch \
            file://0002-waylandsink-add-gbm-based-wlbuf.patch \
            file://0003-gstwaylandsink-support-video-size-change.patch \
            file://0004-wayland-add-NV12_UBWC-format-and-disable-ubwc-property.patch \
            file://0005-gstwaylandsink-remove-EGL_BUFFER_SIZE-attribute.patch \
            file://0006-waylandsink-add-ivi-shell-support.patch \
            file://0007-wayland-workaroud-for-egl-memory-leak-and-runtime-decision.patch \
            file://0008-waylandsink-change-for-video-playback-hang.patch \
            file://0009-waylandsink-change-for-video-playback-fail.patch \
            file://0010-VideoParser-verify-the-layer-encoding.patch \
            file://0011-waylandsink-set-opaque-region-for-video-surface.patch \
            file://0012-waylandsink-refine-the-shell-check.patch \
            file://0013-waylandsink-clean-code-and-indent.patch \
            file://0014-waylandsink-use-gbm-instead-of-shm-for-still-image.patch \
            file://0015-waylandsink-change-a-little-in-configure.ac.patch \
            file://0016-waylandsink-change-gbm-device-node-to-renderD128.patch \
            file://0017-gstwaylandsink-Fix-crash-issue.patch \
            file://0018-waylansink-Calculate-the-latency-of-resolution-chang.patch \
            file://0019-waylandsink-remove-one-useless-log.patch \
            file://0020-waylandsink-fix-gstbuffer-gst_memory_map-issue.patch \
            file://0021-waylandsink-add-GST_LOG_OBJECT-log-after-creating-new-wlbuf.patch \
            file://0022-waylandsink-add-NV12_UBWC-in-sink-pad-cap-template.patch \
            file://0023-waylandsink-rename-gbmbuf-to-gbmbuf_backend.patch \
            file://0024-Optimize-the-latency-of-reallocation-of-wl-buffer.patch \
            file://0025-waylandsink-modify-the-key-rule-of-hash-table.patch \
            file://0026-waylandsink-remove-gstwlbuf-one-by-one-after-resolution-change.patch \
            file://0027-change-meson-for-build.patch \
            file://0028-gstionbuf_meta-remove-duplicated-config.h-including.patch \
            "
DEPENDS += "gbm"

DEPENDS += "wayland-native"
DEPENDS += "weston"

PACKAGECONFIG = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
    orc \
    hls \
    dash \
    "
PACKAGECONFIG_GL = "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2 egl', '', d)}"

PACKAGECONFIG[dc1394]          = "-Ddc1394=enabled,-Ddc1394=disabled,libdc1394"
PACKAGECONFIG[wayland] = "-Dwayland=enabled -Degl=enabled,-Dwayland=disabled -Degl=disabled,wayland virtual/egl"
PACKAGECONFIG[hls]             = "-Dhls=enabled -Dhls-crypto=nettle,-Dhls=disabled,nettle"
ACKAGECONFIG[kms]             = "-Dkms=enabled,-Dkms=disabled,libdrm"
PACKAGECONFIG[openjpeg]        = "-Dopenjpeg=enabled,-Dopenjpeg=disabled,openjpeg"
PACKAGECONFIG[vulkan]          = "-Dvulkan=enabled,-Dvulkan=disabled,vulkan"
PACKAGECONFIG[wayland]         = "-Dwayland=enabled,-Dwayland=disabled,wayland-native wayland wayland-protocols libdrm"

EXTRA_OEMESON = " \
                -Daccurip=disabled \
                -Dacm=disabled \
                -Dadpcmdec=disabled \
                -Dadpcmenc=disabled \
                -Daiff=disabled \
                -Dandroid_media=disabled \
                -Dapexsink=disabled \
                -Dasfmux=disabled \
                -Dassrender=disabled \
                -Daudiofxbad=disabled \
                -Daudiofxbad=disabled \
                -Daudiomixer=disabled \
                -Daudiovisualizers=disabled \
                -Dautoconvert=disabled \
                -Davc=disabled \
                -Dbayer=disabled \
                -Dbluez=disabled \
                -Dbs2b=disabled \
                -Dbz2=disabled \
                -Dcamerabin2=disabled \
                -Dcdxaparse=disabled \
                -Dchromaprint=disabled \
                -Dcocoa=disabled \
                -Dcoloreffects=disabled \
                -Dcompositor=disabled \
                -Dcurl=disabled \
                -Ddaala=disabled \
                -Ddash=enabled \
                -Ddataurisrc=disabled \
                -Ddc1394=disabled \
                -Ddccp=disabled \
                -Ddebugutils=disabled \
                -Ddecklink=disabled \
                -Ddirect3d=disabled \
                -Ddirectfb=disabled \
                -Ddirectsound=disabled \
                -Ddispmanx=disabled \
                -Ddtls=disabled \
                -Ddts=disabled \
                -Ddvb=disabled \
                -Ddvdspu=disabled \
                -Ddvbsuboverlay=disabled \
                -Degl=disabled \
                -Dfaac=disabled \
                -Dfaad=disabled \
                -Dfaceoverlay=disabled \
                -Dfbdev=disabled \
                -Dfestival=disabled \
                -Dfieldanalysis=disabled \
                -Dflite=disabled \
                -Dfluidsynth=disabled \
                -Dfreeverb=disabled \
                -Dfrei0r=disabled \
                -Dgaudieffects=disabled \
                -Dgdp=disabled \
                -Dgeometrictransform=disabled \
                -Dgl=disabled \
                -Dgles2=disabled \
                -Dglx=disabled \
                -Dgme=disabled \
                -Dgsm=disabled \
                -Dgtk3=disabled \
                -Dhdvparse=disabled \
                -Did3tag=enabled \
                -Dinter=disabled \
                -Dinterlace=disabled \
                -Divfparse=disabled \
                -Divtc=disabled\
                -Djp2kdecimator=disabled \
                -Djpegformat=disabled \
                -Dladspa=disabled \
                -Dlibde265=disabled \
                -Dlibmms=disabled \
                -Dlibrfb=disabled \
                -Dlibvisual=disabled \
                -Dlinsys=disabled \
                -Dlv2=disabled \
                -Dkate=disabled \
                -Dmidi=enabled \
                -Dmimic=disabled \
                -Dmodplug=disabled \
                -Dmpeg2enc=disabled \
                -Dmpegdemux=enabled \
                -Dmpegtsdemux=enabled \
                -Dmpegtsmux=enabled \
                -Dmpegpsmux=enabled \
                -Dmplex=disabled \
                -Dmusepack=disabled \
                -Dmve=disabled \
                -Dmxf=disabled \
                -Dnas=disabled \
                -Dneon=disabled \
                -Dnetsim=disabled \
                -Dnuvdemux=disabled \
                -Dnvenc=disabled \
                -Dofa=disabled \
                -Donvif=disabled \
                -Dopenal=disabled \
                -Dopencv=disabled \
                -Dopenexr=disabled \
                -Dopengl=disabled \
                -Dopenh264=disabled \
                -Dopenjpeg=disabled \
                -Dopenni2=disabled \
                -Dopensles=disabled \
                -Dopus=disabled \
                -Dpatchdetect=disabled \
                -Dpcapparse=disabled \
                -Dpnm=disabled \
                -Dpvr=disabled \
                -Dqt=disabled \
                -Drawparse=enabled \
                -Dremovesilence=disabled \
                -Dresindvd=disabled \
                -Drsvg=disabled \
                -Drtmp=disabled \
                -Dsbc=disabled \
                -Dschro=disabled \
                -Dsdi=disabled \
                -Dsdl=disabled \
                -Dsdltest=disabled \
                -Dsdp=disabled \
                -Dsegmentclip=disabled \
                -Dsiren=disabled \
                -Dsmooth=disabled \
                -Dsmoothstreaming=disabled \
                -Dsndfile=disabled \
                -Dsndio=disabled \
                -Dsoundtouch=disabled \
                -Dspandsp=disabled \
                -Dspc=disabled \
                -Dspeed=disabled \
                -Dsrtp=disabled \
                -Dstereo=enabled \
                -Dsubenc=disabled \
                -Dteletextdec=disabled \
                -Dtimidity=disabled \
                -Dtinyalsa=disabled \
                -Dtta=disabled \
                -Duvch264=disabled \
                -Dvcd=disabled \
                -Dvdpau=disabled \
                -Dvideofilters=disabled \
                -Dvideoframe_audiolevel=disabled \
                -Dvideomeasure=disabled \
                -Dvideoparsers=enabled \
                -Dvideosignal=disabled \
                -Dvmnc=disabled \
                -Dvoaacenc=disabled \
                -Dvoamrwbenc=disabled \
                -Dvulkan=disabled \
                -Dwebp=disabled \
                -Dwasapi=disabled \
                -Dwgl=disabled \
                -Dwildmidi=disabled \
                -Dwininet=disabled \
                -Dwinks=disabled \
                -Dwinscreencap=disabled \
                -Dx11=disabled \
                -Dx265=disabled \
                -Dxvid=enabled \
                -Dy4m=disabled \
                -Dyadif=disabled \
                -Dzbar=disabled \
                "
EXTRA_OECONF_append =" --with-protocal-xml-path=${STAGING_DATADIR}/weston"
EXTRA_OEMESON_append = " \
			   -Dkernel_path=${STAGING_KERNEL_BUILDDIR}/usr/include \
              "
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
do_compile_prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/allocators/.libs"
}

