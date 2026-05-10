package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class TrackId(override val value: String) : MediaItemId

sealed interface Track : MediaItem {
    override val id: TrackId
    val trackNumber: Int?
    val albums: PersistentList<SimpleAlbum>
    val notes: String?
}
