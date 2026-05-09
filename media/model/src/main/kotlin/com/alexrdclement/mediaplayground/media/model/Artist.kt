package com.alexrdclement.mediaplayground.media.model

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@JvmInline
@Serializable
value class ArtistId(val value: String)

@Serializable
data class Artist(
    val id: ArtistId,
    val name: String?,
    val notes: String?,
    val createdAt: Instant,
    val modifiedAt: Instant,
)
