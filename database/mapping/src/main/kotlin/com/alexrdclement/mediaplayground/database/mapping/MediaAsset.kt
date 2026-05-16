package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaAssetType
import com.alexrdclement.mediaplayground.database.model.MediaItemType
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import kotlinx.collections.immutable.toPersistentList
import com.alexrdclement.mediaplayground.database.model.AudioAsset as AudioAssetEntity
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset as CompleteAudioAssetEntity
import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.database.model.MediaAsset as MediaAssetRecord

fun AudioAsset.toAudioAssetEntity(): AudioAssetEntity {
    return AudioAssetEntity(
        id = id.value,
        durationUs = metadata.durationUs ?: 0L,
        sampleRate = metadata.sampleRate,
        channelCount = metadata.channelCount ?: 0,
        bitRate = metadata.bitRate,
        bitDepth = metadata.bitDepth,
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
        originUri = originUri,
    )
}

fun AudioAsset.toMediaItemEntity(): MediaItem {
    val fileName = when (val uri = uri) {
        is MediaAssetUri.Shared -> uri.fileName
        is MediaAssetUri.Album -> uri.fileName
    }
    return MediaItem(
        id = id.value,
        itemType = MediaItemType.ASSET,
        title = fileName,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}

fun CompleteAudioAssetEntity.toAudioAsset(): AudioAsset {
    return AudioAsset(
        id = AudioAssetId(audioAsset.id),
        uri = mediaAsset.uri,
        originUri = mediaAsset.originUri,
        createdAt = mediaItem.createdAt,
        modifiedAt = mediaItem.modifiedAt,
        artists = artists.map { it.toArtist() }.toPersistentList(),
        images = images.map { it.toImage() }.toPersistentList(),
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
