#Enable the gcc compile option with libgomp.

EXTRA_OECONF_INITIAL_remove = " --disable-libgomp "
EXTRA_OECONF_INITIAL_append = " --enable-libgomp "

