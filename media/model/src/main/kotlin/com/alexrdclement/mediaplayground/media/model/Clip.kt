package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class ClipId(override val value: String) : MediaItemId

@Serializable
sealed interface Clip : MediaItem {
    override val id: ClipId
}
