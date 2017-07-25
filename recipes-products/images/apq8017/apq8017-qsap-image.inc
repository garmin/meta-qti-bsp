require apq8017-base-image.inc

IMAGE_INSTALL += "libcap"
IMAGE_INSTALL += "attr"
IMAGE_INSTALL += "iw"
IMAGE_INSTALL += "data"
IMAGE_INSTALL += "sigma-dut"
IMAGE_INSTALL += "lib32-ffmpeg"

#Enabling GStreamer Core
IMAGE_INSTALL += "lib32-gstreamer1.0"
IMAGE_INSTALL += "lib32-gstreamer1.0-plugins-base"
IMAGE_INSTALL += "lib32-gstreamer1.0-plugins-good"
IMAGE_INSTALL += "lib32-gstreamer1.0-rtsp-server"
IMAGE_INSTALL += "lib32-gstreamer1.0-plugins-bad"
IMAGE_INSTALL += "lib32-gstreamer1.0-omx"
IMAGE_INSTALL += "lib32-gstreamer1.0-libav"
