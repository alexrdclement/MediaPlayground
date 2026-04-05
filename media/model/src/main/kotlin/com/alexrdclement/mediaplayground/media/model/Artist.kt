package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    val id: String,
    val name: String?,
    val notes: String?,
)
