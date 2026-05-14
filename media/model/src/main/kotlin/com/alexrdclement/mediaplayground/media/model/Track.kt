package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class TrackId(override val value: String) : MediaCollectionId

sealed interface Track : MediaCollection<TrackClip<TimeUnit.Samples>> {
    override val id: TrackId
    val notes: String?
}
