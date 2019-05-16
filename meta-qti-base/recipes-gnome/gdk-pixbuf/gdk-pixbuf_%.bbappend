#Appending the gobject-introspection package to the DEPEND list so the recipe could successfully find introspection.m4 during configure task.

DEPENDS += "gobject-introspection gobject-introspection-native"
DEPENDS += "tiff"
GDK_PIXBUF_LOADERS = "tiff"

