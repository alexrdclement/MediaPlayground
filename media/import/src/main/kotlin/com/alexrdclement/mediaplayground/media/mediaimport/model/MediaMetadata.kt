package com.alexrdclement.mediaplayground.media.mediaimport.model

data class MediaMetadata(
    val title: String?,
    val durationMs: Long?,
    val trackNumber: Int?,
    val artistName: String?,
    val albumTitle: String?,
    val embeddedPicture: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaMetadata

        if (durationMs != other.durationMs) return false
        if (trackNumber != other.trackNumber) return false
        if (title != other.title) return false
        if (artistName != other.artistName) return false
        if (albumTitle != other.albumTitle) return false
        if (!embeddedPicture.contentEquals(other.embeddedPicture)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = durationMs?.hashCode() ?: 0
        result = 31 * result + (trackNumber ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (artistName?.hashCode() ?: 0)
        result = 31 * result + (albumTitle?.hashCode() ?: 0)
        result = 31 * result + embeddedPicture.contentHashCode()
        return result
    }
}
