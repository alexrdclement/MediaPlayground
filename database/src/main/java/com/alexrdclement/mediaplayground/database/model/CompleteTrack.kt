package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CompleteTrack(
    @Embedded
    val track: Track,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "id"
    )
    val album: Album,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumArtistCrossRef::class,
            parentColumn = "albumId",
            entityColumn = "artistId",
        ),
    )
    val artists: List<Artist>,
    @Relation(
        parentColumn = "albumId",
        entityColumn = "albumId"
    )
    val images: List<Image>,
)

val CompleteTrack.id: String
    get() = track.id
