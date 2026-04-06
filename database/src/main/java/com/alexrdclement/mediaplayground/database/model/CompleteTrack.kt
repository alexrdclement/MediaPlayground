package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CompleteTrack(
    @Embedded
    val track: Track,
    @Relation(
        parentColumn = "album_id",
        entityColumn = "id"
    )
    val album: Album,
    @Relation(
        parentColumn = "album_id",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumArtistCrossRef::class,
            parentColumn = "album_id",
            entityColumn = "artist_id",
        ),
    )
    val artists: List<Artist>,
    @Relation(
        parentColumn = "album_id",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumImageCrossRef::class,
            parentColumn = "album_id",
            entityColumn = "image_id",
        ),
    )
    val images: List<ImageFile>,
    @Relation(
        entity = TrackClipCrossRef::class,
        parentColumn = "id",
        entityColumn = "track_id",
    )
    val clips: List<CompleteTrackClip>,
)

val CompleteTrack.id: String
    get() = track.id
