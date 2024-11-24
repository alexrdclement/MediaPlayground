package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteTrack(
    @Embedded
    val track: Track,

    @Relation(
        parentColumn = "albumId",
        entityColumn = "id",
    )
    val album: Album,
)
