package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
data class SimpleArtist(
    val id: String,
    val name: String?,
    val notes: String?,
)
