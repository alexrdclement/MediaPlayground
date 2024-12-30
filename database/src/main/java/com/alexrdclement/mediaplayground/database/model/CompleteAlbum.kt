package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteAlbum(
    @Embedded
    val simpleAlbum: SimpleAlbum,
    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val tracks: List<Track>,
)
