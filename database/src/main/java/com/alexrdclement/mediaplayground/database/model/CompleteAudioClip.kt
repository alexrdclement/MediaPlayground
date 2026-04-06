package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteAudioClip(
    @Embedded
    val clip: Clip,
    @Relation(
        parentColumn = "audio_file_id",
        entityColumn = "id",
    )
    val audioFile: AudioFile,
)
