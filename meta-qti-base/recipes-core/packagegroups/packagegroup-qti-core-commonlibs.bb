SUMMARY = "QTI package group for os common libs"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-core-commonlibs \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    libcutils \
    libstdc++ \
    liblog \
    glib-2.0 \
    procps \
    "
