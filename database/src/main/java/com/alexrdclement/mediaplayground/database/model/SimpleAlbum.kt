package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class SimpleAlbum(
    @Embedded
    val album: Album,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumArtistCrossRef::class,
            parentColumn = "albumId",
            entityColumn = "artistId",
        ),
    )
    val artists: List<Artist>,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId",
    )
    val images: List<Image>,
)
