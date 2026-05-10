package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class AudioAlbumId(override val value: String) : AlbumId

data class AudioAlbum(
    override val id: AudioAlbumId,
    override val title: String,
    override val artists: PersistentList<Artist>,
    override val images: PersistentList<Image>,
    override val items: PersistentList<AudioTrack>,
    val notes: String?,
) : AudioCollection<AudioTrack>
