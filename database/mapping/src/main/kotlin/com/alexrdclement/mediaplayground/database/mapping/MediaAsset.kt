package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.data.disk.mapper.fileNameFromUri
import com.alexrdclement.mediaplayground.data.disk.mapper.uriFromFileName
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.Artist
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.io.files.Path
import com.alexrdclement.mediaplayground.database.model.AudioFile as AudioFileEntity

fun MediaAsset.toAudioFileEntity(): AudioFileEntity {
    val metadata = metadata as? MediaMetadata.Audio
        ?: error("MediaAsset metadata must be Audio")
    val fileName = fileNameFromUri(uri)
    require(fileName != null) { "MediaAsset uri must be a file uri" }
    return AudioFileEntity(
        id = id.value,
        fileName = fileName,
        source = source.toEntitySource(),
        durationUs = metadata.durationUs ?: 0L,
        sampleRate = metadata.sampleRate ?: 0,
        channelCount = metadata.channelCount ?: 0,
        bitRate = metadata.bitRate ?: 0,
        bitDepth = metadata.bitDepth ?: 0,
        mimeType = metadata.mimeType ?: "",
        extension = metadata.extension,
    )
}

fun AudioFileEntity.toMediaAsset(): MediaAsset {
    return toMediaAsset(
        uri = null,
        artists = persistentListOf(),
        images = persistentListOf(),
    )
}

fun AudioFileEntity.toMediaAsset(
    albumDir: Path,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): MediaAsset {
    return toMediaAsset(
        uri = uriFromFileName(albumDir, fileName),
        artists = artists,
        images = images,
    )
}

private fun AudioFileEntity.toMediaAsset(
    uri: String?,
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): MediaAsset {
    return MediaAsset(
        id = MediaAssetId(id),
        uri = uri,
        source = source.toDomainSource(),
        artists = artists,
        images = images,
        metadata = MediaMetadata.Audio(
            title = null,
            durationUs = durationUs,
            sampleRate = sampleRate,
            channelCount = channelCount,
            bitRate = bitRate,
            bitDepth = bitDepth,
            trackNumber = null,
            artistName = null,
            albumTitle = null,
            embeddedPicture = null,
            mimeType = mimeType,
            extension = extension,
        ),
    )
}
