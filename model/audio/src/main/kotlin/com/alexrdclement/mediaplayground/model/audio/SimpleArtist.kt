package com.alexrdclement.mediaplayground.model.audio

import kotlinx.serialization.Serializable

@Serializable
data class SimpleArtist(
    val id: String,
    val name: String?,
)
