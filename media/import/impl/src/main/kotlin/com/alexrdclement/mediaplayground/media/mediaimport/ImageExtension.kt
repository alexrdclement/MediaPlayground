package com.alexrdclement.mediaplayground.media.mediaimport

private val JpegSignature = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte())
private val PngSignature = byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte())

internal fun ByteArray.imageExtension(): String = when {
    startsWith(JpegSignature) -> "jpg"
    startsWith(PngSignature) -> "png"
    else -> "jpg"
}

private fun ByteArray.startsWith(prefix: ByteArray): Boolean =
    size >= prefix.size && prefix.indices.all { index -> this[index] == prefix[index] }
