PACKAGECONFIG ??= "avdevice avfilter avcodec avformat swresample swscale postproc avresample \
                   alsa bzlib lzma theora zlib \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xv xcb', '', d)}"