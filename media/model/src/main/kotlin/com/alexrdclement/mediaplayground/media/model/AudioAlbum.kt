package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@JvmInline
@Serializable
value class AudioAlbumId(override val value: String) : AlbumId

data class AudioAlbum(
    override val id: AudioAlbumId,
    override val title: String,
    override val artists: PersistentList<Artist>,
    override val images: PersistentList<Image>,
    override val items: PersistentList<AlbumTrack>,
    val notes: String?,
    override val createdAt: Instant,
    override val modifiedAt: Instant,
) : AudioCollection<AlbumTrack> {
}
