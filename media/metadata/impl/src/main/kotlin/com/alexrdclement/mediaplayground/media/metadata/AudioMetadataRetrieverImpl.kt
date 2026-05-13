package com.alexrdclement.mediaplayground.media.metadata

import android.app.Application
import android.media.AudioFormat
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import android.os.Build
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import dev.zacsweers.metro.Inject
import kotlinx.io.files.Path
import android.media.MediaMetadataRetriever as AndroidMediaMetadataRetriever

class AudioMetadataRetrieverImpl @Inject constructor(
    private val application: Application,
) {

    fun getAudioMetadata(
        filePath: Path,
        mimeType: String?,
        extension: String,
    ): MediaMetadata.Audio {
        val path = filePath.toString()
        val mediaFormat = useMediaExtractor(path) {
            extractFormat()
        }
        return AndroidMediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(path)
            buildAudioMetadata(
                retriever = retriever,
                mediaFormat = mediaFormat,
                mimeType = mimeType,
                extension = extension,
            )
        }
    }

    fun getAudioMetadata(
        contentUri: Uri,
        mimeType: String?,
        extension: String,
    ): MediaMetadata.Audio {
        val mediaFormat = useMediaExtractor(contentUri) {
            extractFormat()
        }
        return AndroidMediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(application, contentUri)
            buildAudioMetadata(
                retriever = retriever,
                mediaFormat = mediaFormat,
                mimeType = mimeType,
                extension = extension,
            )
        }
    }

    private fun buildAudioMetadata(
        retriever: AndroidMediaMetadataRetriever,
        mediaFormat: MediaFormat,
        mimeType: String?,
        extension: String,
    ): MediaMetadata.Audio = MediaMetadata.Audio(
        title = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_TITLE),
        durationUs = mediaFormat.getLongOrNull(MediaFormat.KEY_DURATION),
        sampleRate = mediaFormat.getIntOrNull(MediaFormat.KEY_SAMPLE_RATE) ?: 44100,
        channelCount = mediaFormat.getIntOrNull(MediaFormat.KEY_CHANNEL_COUNT),
        bitRate = mediaFormat.getIntOrNull(MediaFormat.KEY_BIT_RATE),
        bitDepth = mediaFormat.getBitDepthOrNull(),
        trackNumber = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
            ?.toIntOrNull(),
        artistName = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ARTIST),
        albumArtistName = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ALBUMARTIST),
        albumTitle = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ALBUM),
        embeddedPicture = retriever.embeddedPicture,
        mimeType = mimeType,
        extension = extension,
    )

    private fun <T> useMediaExtractor(path: String, block: MediaExtractor.() -> T): T {
        val mediaExtractor = MediaExtractor()
        try {
            mediaExtractor.setDataSource(path)
            return block(mediaExtractor)
        } catch (e: Exception) {
            error("Failed to extract audio metadata for $path: $e")
        } finally {
            mediaExtractor.release()
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

    private fun MediaFormat.getBitDepthOrNull(): Int? {
        return when (val encoding = this.getIntOrNull(MediaFormat.KEY_PCM_ENCODING)) {
            AudioFormat.ENCODING_PCM_8BIT -> 8
            AudioFormat.ENCODING_PCM_16BIT -> 16
            AudioFormat.ENCODING_PCM_32BIT -> 32
            AudioFormat.ENCODING_PCM_FLOAT -> 32
            else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && encoding == AudioFormat.ENCODING_PCM_24BIT_PACKED) 24 else null
        }
    }

    private fun MediaFormat.getIntOrNull(key: String): Int? {
        return if (containsKey(key)) getInteger(key) else null
    }

    private fun MediaFormat.getLongOrNull(key: String): Long? {
        return if (containsKey(key)) getLong(key) else null
    }
}
