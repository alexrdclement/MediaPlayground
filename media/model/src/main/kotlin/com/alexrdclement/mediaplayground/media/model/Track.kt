package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.microseconds

@JvmInline
@Serializable
value class TrackId(override val value: String) : MediaItemId

@Serializable
data class Track(
    override val id: TrackId,
    override val title: String,
    override val artists: PersistentList<Artist>,
    val trackNumber: Int?,
    val clips: PersistentSet<TrackClip>,
    val simpleAlbum: SimpleAlbum,
    val notes: String?,
) : MediaItem {
    override val images: PersistentList<Image>  = simpleAlbum.images

    override val source: Source = clips.firstOrNull()?.clip?.source ?: Source.Local

    override val duration: Duration = clips.firstOrNull()?.clip?.duration ?: 0.microseconds

    override val isPlayable: Boolean = clips.isNotEmpty()
}
