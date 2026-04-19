package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
sealed class MediaMetadata {
    abstract val mimeType: String?
    abstract val extension: String

    @Serializable
    data class Audio(
        override val mimeType: String?,
        override val extension: String,
        val title: String?,
        val sampleRate: Int,
        val durationUs: Long?,
        val channelCount: Int?,
        val bitRate: Int?,
        val bitDepth: Int?,
        val trackNumber: Int?,
        val artistName: String?,
        val albumTitle: String?,
        val embeddedPicture: ByteArray?,
    ) : MediaMetadata() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Audio
            if (sampleRate != other.sampleRate) return false
            if (durationUs != other.durationUs) return false
            if (channelCount != other.channelCount) return false
            if (bitRate != other.bitRate) return false
            if (bitDepth != other.bitDepth) return false
            if (trackNumber != other.trackNumber) return false
            if (title != other.title) return false
            if (artistName != other.artistName) return false
            if (albumTitle != other.albumTitle) return false
            if (!embeddedPicture.contentEquals(other.embeddedPicture)) return false
            if (mimeType != other.mimeType) return false
            if (extension != other.extension) return false
            return true
        }

        override fun hashCode(): Int {
            var result = durationUs?.hashCode() ?: 0
            result = 31 * result + sampleRate
            result = 31 * result + (channelCount ?: 0)
            result = 31 * result + (bitRate ?: 0)
            result = 31 * result + (bitDepth ?: 0)
            result = 31 * result + (trackNumber ?: 0)
            result = 31 * result + (title?.hashCode() ?: 0)
            result = 31 * result + (artistName?.hashCode() ?: 0)
            result = 31 * result + (albumTitle?.hashCode() ?: 0)
            result = 31 * result + embeddedPicture.contentHashCode()
            result = 31 * result + (mimeType?.hashCode() ?: 0)
            result = 31 * result + extension.hashCode()
            return result
        }
    }

    @Serializable
    data class Image(
        override val mimeType: String,
        override val extension: String,
        val widthPx: Int,
        val heightPx: Int,
        val dateTimeOriginal: String?,
        val gpsLatitude: Double?,
        val gpsLongitude: Double?,
        val cameraMake: String?,
        val cameraModel: String?,
    ) : MediaMetadata()
}
