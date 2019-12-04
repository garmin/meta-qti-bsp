DEFAULT_PREFERENCE = "-1"

SRC_URI_remove = " \
    file://0001-Makefile.am-don-t-hardcode-libtool-name-when-running.patch \
"
SRC_URI_remove += " file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch"
DEPENDS += "gbm"


SRC_URI = "${PATH_TO_REPO}/gstreamer/gst-plugins-bad/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-bad;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-bad/common;branch=gstreamer/common/master;name=common"

GI_DATA_ENABLED="0"
SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "bad_common"
S = "${WORKDIR}/gstreamer/gst-plugins-bad"

DEPENDS += "wayland-native"
DEPENDS += "weston"

PACKAGECONFIG = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
    orc \
    hls \
    dash \
    "
PACKAGECONFIG_GL = "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2 egl', '', d)}"

PACKAGECONFIG[dc1394]          = "--enable-dc1394,--disable-dc1394,libdc1394"
PACKAGECONFIG[wayland] = "--enable-wayland --enable-egl,--disable-wayland --disable-egl,wayland virtual/egl"
PACKAGECONFIG[hls]             = "--enable-hls --with-hls-crypto=nettle,--disable-hls,nettle"
ACKAGECONFIG[kms]             = "--enable-kms,--disable-kms,libdrm"
PACKAGECONFIG[openjpeg]        = "--enable-openjpeg,--disable-openjpeg,openjpeg"
PACKAGECONFIG[vulkan]          = "--enable-vulkan,--disable-vulkan,vulkan"
PACKAGECONFIG[wayland]         = "--enable-wayland,--disable-wayland,wayland-native wayland wayland-protocols libdrm"

EXTRA_OECONF = " \
                --disable-accurip \
                --disable-acm \
                --disable-adpcmdec \
                --disable-adpcmenc \
                --disable-aiff \
                --disable-android_media \
                --disable-apexsink \
                --disable-asfmux \
                --disable-assrender \
                --disable-audiofxbad \
                --disable-audiofxbad \
                --disable-audiomixer \
                --disable-audiovisualizers \
                --disable-autoconvert \
                --disable-avc \
                --disable-bayer \
                --disable-bluez \
                --disable-bs2b \
                --disable-bz2 \
                --disable-camerabin2 \
                --disable-cdxaparse \
                --disable-chromaprint \
                --disable-cocoa \
                --disable-coloreffects \
                --disable-compositor \
                --disable-curl \
                --disable-daala \
                --enable-dash \
                --disable-dataurisrc \
                --disable-dc1394 \
                --disable-dccp \
                --disable-debugutils \
                --disable-decklink \
                --disable-direct3d \
                --disable-directfb \
                --disable-directsound \
                --disable-dispmanx \
                --disable-dtls \
                --disable-dts \
                --disable-dvb \
                --disable-dvdspu \
                --disable-dvbsuboverlay \
                --disable-egl \
                --disable-faac \
                --disable-faad \
                --disable-faceoverlay \
                --disable-fbdev \
                --disable-festival \
                --disable-fieldanalysis \
                --disable-flite \
                --disable-fluidsynth \
                --disable-freeverb \
                --disable-frei0r \
                --disable-gaudieffects \
                --disable-gdp \
                --disable-geometrictransform \
                --disable-gl \
                --disable-gles2 \
                --disable-glx \
                --disable-gme \
                --disable-gsm \
                --disable-gtk3 \
                --disable-hdvparse \
                --enable-id3tag \
                --disable-inter \
                --disable-interlace \
                --disable-ivfparse \
                --disable-ivtc \
                --disable-jp2kdecimator \
                --disable-jpegformat \
                --disable-ladspa \
                --disable-libde265 \
                --disable-libmms \
                --disable-librfb \
                --disable-libvisual \
                --disable-linsys \
                --disable-lv2 \
                --disable-kate \
                --enable-midi \
                --disable-mimic \
                --disable-modplug \
                --disable-mpeg2enc \
                --enable-mpegdemux \
                --enable-mpegtsdemux \
                --enable-mpegtsmux \
                --enable-mpegpsmux \
                --disable-mplex \
                --disable-musepack \
                --disable-mve \
                --disable-mxf \
                --disable-nas \
                --disable-neon \
                --disable-netsim \
                --disable-nuvdemux \
                --disable-nvenc \
                --disable-ofa \
                --disable-onvif \
                --disable-openal \
                --disable-opencv \
                --disable-openexr \
                --disable-opengl \
                --disable-openh264 \
                --disable-openjpeg \
                --disable-openni2 \
                --disable-opensles \
                --disable-opus \
                --disable-patchdetect \
                --disable-pcapparse \
                --disable-pnm \
                --disable-pvr \
                --disable-qt \
                --enable-rawparse \
                --disable-removesilence \
                --disable-resindvd \
                --disable-rsvg \
                --disable-rtmp \
                --disable-sbc \
                --disable-schro \
                --disable-sdi \
                --disable-sdl \
                --disable-sdltest \
                --disable-sdp \
                --disable-segmentclip \
                --disable-siren \
                --disable-smooth \
                --disable-smoothstreaming \
                --disable-sndfile \
                --disable-sndio \
                --disable-soundtouch \
                --disable-spandsp \
                --disable-spc \
                --disable-speed \
                --disable-srtp \
                --enable-stereo \
                --disable-subenc \
                --disable-teletextdec \
                --disable-timidity \
                --disable-tinyalsa \
                --disable-tta \
                --disable-uvch264 \
                --disable-vcd \
                --disable-vdpau \
                --disable-videofilters \
                --disable-videoframe_audiolevel \
                --disable-videomeasure \
                --enable-videoparsers \
                --disable-videosignal \
                --disable-vmnc \
                --disable-voaacenc \
                --disable-voamrwbenc \
                --disable-vulkan \
                --disable-webp \
                --disable-wasapi \
                --disable-wgl \
                --disable-wildmidi \
                --disable-wininet \
                --disable-winks \
                --disable-winscreencap \
                --disable-x11 \
                --disable-x265 \
                --enable-xvid \
                --disable-y4m \
                --disable-yadif \
                --disable-zbar \
                "
EXTRA_OECONF_append =" --with-protocal-xml-path=${STAGING_DATADIR}/weston"
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
do_compile_prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/allocators/.libs"
}

