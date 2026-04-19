package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteAlbumRef(
    @Embedded
    val albumTrackCrossRef: AlbumTrackCrossRef,
    @Relation(
        entity = Album::class,
        parentColumn = "album_id",
        entityColumn = "id",
    )
    val simpleAlbum: SimpleAlbum,
)
