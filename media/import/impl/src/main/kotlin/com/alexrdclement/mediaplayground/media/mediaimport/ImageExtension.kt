package com.alexrdclement.mediaplayground.media.mediaimport

internal fun ByteArray.imageExtension(): String = when {
    size >= 3 && this[0] == 0xFF.toByte() && this[1] == 0xD8.toByte() && this[2] == 0xFF.toByte() -> "jpg"
    size >= 4 && this[0] == 0x89.toByte() && this[1] == 0x50.toByte() && this[2] == 0x4E.toByte() && this[3] == 0x47.toByte() -> "png"
    else -> "jpg"
}
