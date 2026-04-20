package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaAssetType
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import com.alexrdclement.mediaplayground.database.model.AudioAsset as AudioAssetEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset as CompleteAudioAssetEntity
import com.alexrdclement.mediaplayground.database.model.MediaAsset as MediaAssetRecord

fun AudioAsset.toAudioAssetEntity(): AudioAssetEntity {
    return AudioAssetEntity(
        id = id.value,
        durationUs = metadata.durationUs ?: 0L,
        sampleRate = metadata.sampleRate,
        channelCount = metadata.channelCount ?: 0,
        bitRate = metadata.bitRate ?: 0,
        bitDepth = metadata.bitDepth ?: 0,
    )
}

fun MediaAsset.toMediaAssetRecord(): MediaAssetRecord = when (this) {
    is AudioAsset -> toMediaAssetRecord()
    is Image -> toMediaAssetRecord()
}

fun AudioAsset.toMediaAssetRecord(): MediaAssetRecord {
    val fileName = when (val uri = uri) {
        is MediaAssetUri.Shared -> uri.fileName
        is MediaAssetUri.Album -> uri.fileName
    }
    return MediaAssetRecord(
        id = id.value,
        uri = uri,
        mediaAssetType = MediaAssetType.AUDIO,
        fileName = fileName,
        mimeType = metadata.mimeType ?: "",
        extension = metadata.extension,
        createdAt = createdAt,
        modifiedAt = createdAt,
        originUri = originUri,
    )
}

fun CompleteAudioAssetEntity.toAudioAsset(): AudioAsset {
    return toAudioAsset(
        artists = persistentListOf(),
        images = persistentListOf(),
    )
}

fun CompleteAudioAssetEntity.toAudioAsset(
    artists: PersistentList<Artist>,
    images: PersistentList<Image>,
): AudioAsset {
    return AudioAsset(
        id = AudioAssetId(audioAsset.id),
        uri = mediaAsset.uri,
        originUri = mediaAsset.originUri,
        createdAt = mediaAsset.createdAt,
        artists = artists,
        images = images,
        metadata = MediaMetadata.Audio(
            title = null,
            durationUs = audioAsset.durationUs,
            sampleRate = audioAsset.sampleRate,
            channelCount = audioAsset.channelCount,
            bitRate = audioAsset.bitRate,
            bitDepth = audioAsset.bitDepth,
            trackNumber = null,
            artistName = null,
            albumTitle = null,
            embeddedPicture = null,
            mimeType = mediaAsset.mimeType,
            extension = mediaAsset.extension,
        ),
    )
}
