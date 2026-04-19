package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteAudioClip(
    @Embedded
    val clip: Clip,
    @Relation(
        parentColumn = "asset_id",
        entityColumn = "id",
    )
    val audioAsset: AudioAsset,
    @Relation(
        parentColumn = "asset_id",
        entityColumn = "id",
    )
    val mediaAsset: MediaAsset,
)
