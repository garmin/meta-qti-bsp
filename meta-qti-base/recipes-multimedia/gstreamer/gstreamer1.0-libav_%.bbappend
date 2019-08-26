EXTRA_OECONF += "--without-system-libav"
SRC_URI_remove += " file://mips64_cpu_detection.patch \
                    file://gtkdoc-no-tree.patch \
                  "

LIBAV_EXTRA_CONFIGURE_COMMON_ARG += "--disable-everything \
                                     --enable-decoder=aac,alac,mp3,wmalossless,wmapro,wmav1,wmav2,wmavoice,dca,eac3,ape \
                                     --enable-demuxer=ape \
                                     "
