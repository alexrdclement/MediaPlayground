package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteAudioAsset(
    @Embedded
    val audioAsset: AudioAsset,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val mediaAsset: MediaAsset,
)
