package com.alexrdclement.mediaplayground.media.metadata

import android.app.Application
import android.media.AudioFormat
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import android.os.Build
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import dev.zacsweers.metro.Inject
import android.media.MediaMetadataRetriever as AndroidMediaMetadataRetriever

class AudioMetadataRetrieverImpl @Inject constructor(
    private val application: Application,
) {
    private data class FormatInfo(
        val sampleRate: Int,
        val durationUs: Long?,
        val channelCount: Int?,
        val bitRate: Int?,
        val bitDepth: Int?,
    )

    fun getAudioMetadata(
        contentUri: Uri,
        mimeType: String?,
        extension: String,
    ): MediaMetadata.Audio {
        val formatInfo = useMediaExtractor(contentUri) {
            val format = extractFormat()
            FormatInfo(
                sampleRate = extractSampleRate(format) ?: 44100,
                durationUs = extractDurationUs(format),
                channelCount = extractChannelCount(format),
                bitRate = extractBitRate(format),
                bitDepth = extractBitDepth(format),
            )
        }
        return AndroidMediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(application, contentUri)
            MediaMetadata.Audio(
                title = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_TITLE),
                durationUs = formatInfo.durationUs,
                sampleRate = formatInfo.sampleRate,
                channelCount = formatInfo.channelCount,
                bitRate = formatInfo.bitRate,
                bitDepth = formatInfo.bitDepth,
                trackNumber = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                    ?.toIntOrNull(),
                artistName = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ARTIST),
                albumTitle = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ALBUM),
                embeddedPicture = retriever.embeddedPicture,
                mimeType = mimeType,
                extension = extension,
            )
        }
    }

    private fun <T> useMediaExtractor(contentUri: Uri, block: MediaExtractor.() -> T): T {
        val mediaExtractor = MediaExtractor()
        try {
            mediaExtractor.setDataSource(application, contentUri, null)
            return block(mediaExtractor)
        } catch (e: Exception) {
            error("Failed to extract audio metadata for $contentUri: $e")
        } finally {
            mediaExtractor.release()
        }
    }

    private fun MediaExtractor.extractFormat(): MediaFormat {
        return (0 until trackCount)
            .map { getTrackFormat(it) }
            .firstOrNull { it.getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true }
            ?: error("No audio track found")
    }

    private fun MediaExtractor.extractSampleRate(format: MediaFormat): Int? {
        return if (format.containsKey(MediaFormat.KEY_SAMPLE_RATE)) {
            format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
        } else {
            null
        }
    }

    private fun MediaExtractor.extractDurationUs(format: MediaFormat): Long? {
        return if (format.containsKey(MediaFormat.KEY_DURATION)) {
            format.getLong(MediaFormat.KEY_DURATION)
        } else {
            null
        }
    }

    private fun MediaExtractor.extractChannelCount(format: MediaFormat): Int? {
        return if (format.containsKey(MediaFormat.KEY_CHANNEL_COUNT)) {
            format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
        } else {
            null
        }
    }

    private fun MediaExtractor.extractBitRate(format: MediaFormat): Int? {
        return if (format.containsKey(MediaFormat.KEY_BIT_RATE)) {
            format.getInteger(MediaFormat.KEY_BIT_RATE)
        } else {
            null
        }
    }

    private fun MediaExtractor.extractBitDepth(format: MediaFormat): Int? {
        if (!format.containsKey(MediaFormat.KEY_PCM_ENCODING)) return null
        return when (val encoding = format.getInteger(MediaFormat.KEY_PCM_ENCODING)) {
            AudioFormat.ENCODING_PCM_8BIT -> 8
            AudioFormat.ENCODING_PCM_16BIT -> 16
            AudioFormat.ENCODING_PCM_32BIT -> 32
            AudioFormat.ENCODING_PCM_FLOAT -> 32
            else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && encoding == AudioFormat.ENCODING_PCM_24BIT_PACKED) 24 else null
        }
    }
}
