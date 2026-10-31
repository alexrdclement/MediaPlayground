package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface MediaMetadata {
    val mimeType: String?
    val extension: String

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
        val albumArtistName: String? = null,
        val albumTitle: String?,
        val embeddedPicture: ByteArray?,
    ) : MediaMetadata {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Audio) return false

            return fieldsWithoutEmbeddedPicture() == other.fieldsWithoutEmbeddedPicture() &&
                embeddedPicture.contentEquals(other.embeddedPicture)
        }

        private fun fieldsWithoutEmbeddedPicture(): List<Any?> = listOf(
            durationUs,
            sampleRate,
            channelCount,
            bitRate,
            bitDepth,
            trackNumber,
            title,
            artistName,
            albumArtistName,
            albumTitle,
            mimeType,
            extension,
        )

        override fun hashCode(): Int {
            var result = durationUs?.hashCode() ?: 0
            result = 31 * result + sampleRate
            result = 31 * result + (channelCount ?: 0)
            result = 31 * result + (bitRate ?: 0)
            result = 31 * result + (bitDepth ?: 0)
            result = 31 * result + (trackNumber ?: 0)
            result = 31 * result + (title?.hashCode() ?: 0)
            result = 31 * result + (artistName?.hashCode() ?: 0)
            result = 31 * result + (albumArtistName?.hashCode() ?: 0)
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
    ) : MediaMetadata
}
