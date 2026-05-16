package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteImageAsset(
    @Embedded
    val imageAsset: ImageAsset,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val mediaItem: MediaItem,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val mediaAsset: MediaAsset,
)

val CompleteImageAsset.id: String
    get() = imageAsset.id
