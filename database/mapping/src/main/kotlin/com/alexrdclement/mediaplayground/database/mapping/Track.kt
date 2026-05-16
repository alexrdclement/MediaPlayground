package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaCollectionType
import com.alexrdclement.mediaplayground.database.model.MediaItemType
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.media.model.toKotlinDuration
import kotlinx.collections.immutable.toPersistentList
import com.alexrdclement.mediaplayground.database.model.CompleteTrack as CompleteTrackEntity
import com.alexrdclement.mediaplayground.database.model.MediaCollection as MediaCollectionEntity
import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.database.model.Track as TrackEntity

fun AudioTrack.toTrackEntity(): TrackEntity {
    return TrackEntity(
        id = id.value,
        notes = notes,
    )
}

fun AudioTrack.toMediaCollectionEntity(): MediaCollectionEntity {
    return MediaCollectionEntity(
        id = id.value,
        mediaCollectionType = MediaCollectionType.TRACK,
    )
}

fun AudioTrack.toMediaItemEntity(): MediaItem {
    return MediaItem(
        id = id.value,
        itemType = MediaItemType.COLLECTION,
        title = title,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}

fun CompleteTrackEntity.toAudioTrack(): AudioTrack {
    return AudioTrack(
        id = TrackId(track.id),
        title = mediaItem.title,
        items = clips.map { it.toTrackClip() }.sortedBy { it.trackOffset.toKotlinDuration() }.toPersistentList(),
        notes = track.notes,
        createdAt = mediaItem.createdAt,
        modifiedAt = mediaItem.modifiedAt,
    )
}
