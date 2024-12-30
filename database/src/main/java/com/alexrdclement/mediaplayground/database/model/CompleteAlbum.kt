package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteAlbum(
    @Embedded
    val albumWithArtists: AlbumWithArtists,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val images: List<Image>,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val tracks: List<Track>,
)
