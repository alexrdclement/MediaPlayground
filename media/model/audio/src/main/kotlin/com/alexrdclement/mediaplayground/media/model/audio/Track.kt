package com.alexrdclement.mediaplayground.media.model.audio

import com.alexrdclement.mediaplayground.media.model.image.Image
import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@JvmInline
@Serializable
value class TrackId(override val value: String) : MediaItemId

@Serializable
data class Track(
    override val id: TrackId,
    override val title: String,
    override val artists: PersistentList<SimpleArtist>,
    override val duration: Duration,
    val trackNumber: Int?,
    val uri: String?,
    val simpleAlbum: SimpleAlbum,
    override val source: Source,
    val notes: String?,
) : MediaItem {
    override val images: PersistentList<Image>
        get() = simpleAlbum.images

    override val isPlayable: Boolean
        get() = uri != null
}
