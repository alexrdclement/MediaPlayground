package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.database.model.MediaCollectionType
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.collections.immutable.toPersistentSet
import com.alexrdclement.mediaplayground.database.model.CompleteTrack as CompleteTrackEntity
import com.alexrdclement.mediaplayground.database.model.MediaCollection as MediaCollectionEntity
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
        title = title,
        mediaCollectionType = MediaCollectionType.TRACK,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
    )
}

fun CompleteTrackEntity.toAudioTrack(): AudioTrack {
    return AudioTrack(
        id = TrackId(track.id),
        title = mediaCollection.title,
        clips = clips.map { it.toTrackClip() }.toPersistentSet(),
        notes = track.notes,
        createdAt = mediaCollection.createdAt,
        modifiedAt = mediaCollection.modifiedAt,
    )
}
