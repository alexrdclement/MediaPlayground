package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class TrackId(override val value: String) : MediaItemId

sealed interface Track : MediaItem {
    override val id: TrackId
    val notes: String?
}
