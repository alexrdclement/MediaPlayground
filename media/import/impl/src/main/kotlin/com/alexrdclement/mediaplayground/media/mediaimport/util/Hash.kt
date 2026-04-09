package com.alexrdclement.mediaplayground.media.mediaimport.util

import java.security.MessageDigest

internal fun ByteArray.sha256(): String =
    MessageDigest.getInstance("SHA-256")
        .digest(this)
        .joinToString("") { "%02x".format(it) }
