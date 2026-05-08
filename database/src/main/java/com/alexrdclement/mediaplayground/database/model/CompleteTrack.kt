package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteTrack(
    @Embedded
    val track: Track,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val mediaCollection: MediaCollection,
    @Relation(
        entity = AlbumTrackCrossRef::class,
        parentColumn = "id",
        entityColumn = "track_id",
    )
    val albumRefs: List<CompleteAlbumRef>,
    @Relation(
        entity = TrackClipCrossRef::class,
        parentColumn = "id",
        entityColumn = "track_id",
    )
    val clips: List<CompleteTrackClip>,
)

val CompleteTrack.id: String
    get() = track.id
