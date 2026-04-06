package com.alexrdclement.mediaplayground.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class CompleteTrackClip(
    @Embedded val trackClipCrossRef: TrackClipCrossRef,
    @Relation(
        entity = Clip::class,
        parentColumn = "clip_id",
        entityColumn = "id",
    )
    val completeAudioClip: CompleteAudioClip,
)
