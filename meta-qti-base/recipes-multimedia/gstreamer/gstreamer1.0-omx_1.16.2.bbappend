DEFAULT_PREFERENCE = "-1"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base gstreamer1.0-plugins-bad"
DEPENDS += "media"
RDEPENDS_${PN} = "media"
GSTREAMER_1_0_OMX_TARGET = "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"

FILESEXTRAPATHS_append := ":${THISDIR}/gst-omx"
SRC_URI += "file://0001-gst-omx-add-qti-openmax-il-target.patch \
            file://0002-gst-omx-implement-share-buffer-mode-for-gst-omx-waylandsink.patch \
            file://0003-gst-omx-Add-NV12_UBWC-and-RGBA_UBWC.patch \
            file://0004-gstomxvideodec-update-output-port-for-decoder.patch \
            file://0005-gstomxvideodec-resolve-incorrect-process-in-_omx_out_buffer_destroy.patch \
            file://0006-omxvideodec-Handle-the-port-rectangle-change-event.patch \
            file://0007-gst-omx-change-for-height-error-of-h265-dec.patch \
            file://0008-gst-omx-support-gbm-buffer-backend-disable-some-dec-pool-code.patch \
            file://0009-gst-omx-enc-change-for-encode-failed.patch \
            file://0010-gst-omx-enc-change-for-encode-bitrate-error.patch \
            file://0011-omx-omxvideodec-refine-gbm-check.patch \
            file://0012-gst-omx-h265enc-SPS-PPS-generated-with-IDR-frame.patch \
            file://0013-gst-omx-h264enc-SPS-PPS-generated-with-IDR-frame.patch \
            file://0014-gst-omx-add-vp9-decoder.patch \
            file://0015-omx-omxvideodec-refine-gstbuffer-and-some-logs.patch \
            file://0016-omxvideodec-resolve-video-black-screen.patch \
            file://0017-gst-omx-Enable-WMV-VC-1-decoder-plugin.patch \
            file://0018-gst-omx-Enable-DivX-decoder-plugin.patch \
            file://0019-gst-omx-support-vp8-encoder.patch \
            file://0020-revert-for-we-have-update-nBufferCountMin-in-swdec.patch \
            file://0021-Fix-hang-when-play-the-short-clips.patch \
            file://0022-omxvidodec-support-multiresolution-playback.patch \
            file://0023-omxvideodec-support-other-multi-resolution-corner-cases.patch \
            file://0024-Optimize-the-startup-speed-of-some-streams.patch \
            file://0025-Add-plugin-of-HEIC-encoder.patch \
            file://0026-omxvideoenc-dynamic-frame-rate-setting-support.patch \
            file://0027-gst-omx-Add-support-for-multi-slice-in-H264-and-H265.patch \
            file://0028-gst-omx-initial-QP-interface.patch \
            file://0029-gst-omx-add-RC-parameter-MaxBitrate-and-MaxBitrate-skip-frame.patch \
            file://0030-gstomxvideoenc-add-property-to-set-min-and-max-QP.patch \
            file://0031-gstomx-enc-Support-rotation-and-flip.patch \
            file://0032-gstomx-Enc-Support-intra-MB-refresh.patch \
            file://0033-gstomx-enc-support-downscale-encoding.patch \
            file://0034-omxvideodec-set-component-to-OMX_StateIdle-instead-of-OMX_StatePause.patch \
            file://0035-gstomx-Enc-Support-crop-of-encoder.patch \
            file://0036-gst-omx-dec-attach-omx-filled-data-length-on-output-gstbuffer.patch \
            file://0037-gst-omx-change-for-encode-share-buffer.patch \
            file://0038-Optimize-the-latency-of-resolution-change.patch \
            file://0039-omx-video-enc-remove-FractionToQ16-definition.patch \
            file://0040-omxvideodec-push-the-cached-segment-event-when-seek-multi-resolution-clips.patch \
            file://0041-The-crop-event-need-be-handled-before-eos-event.patch \
            file://0042-change-meson-for-build.patch \
            "

EXTRA_OEMESON = " \
               -Dtarget=qti \
               -Dheader_path=${STAGING_INCDIR}/mm-core \
			   -Dkernel_path=${STAGING_KERNEL_BUILDDIR}/usr/include \
			   -Dstaging_inc_path=${STAGING_INCDIR} \
              "
EXTRA_OEMESON_append =" -Denable-target-vpu554=yes"
EXTRA_OEMESON_append =" -Denable-encoder-heic=yes"

CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

delete_pkg_m4_file() {
    # Delete m4 files which we provide patched versions of but will be ignored
    # if these exist
	rm -f "${S}/common/m4/pkg.m4"
	rm -f "${S}/common/m4/gtk-doc.m4"
}

do_configure[prefuncs] += "delete_pkg_m4_file"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}