package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteAlbum(
    @Embedded
    val simpleAlbum: SimpleAlbum,
    @Relation(
        parentColumn = "id",
        entityColumn = "album_id"
    )
    val tracks: List<Track>,
)

val CompleteAlbum.id: String
    get() = simpleAlbum.album.id
