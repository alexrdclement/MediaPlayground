package com.alexrdclement.mediaplayground.model.audio

import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.Serializable

@Serializable
data class SimpleAlbum(
    val id: AlbumId,
    val name: String,
    val artists: PersistentList<SimpleArtist>,
    val images: PersistentList<Image>,
    val source: Source,
)

val SimpleAlbum.thumbnailImageUrl: String?
    get() {
        // Third image is typically too low-res
        val image = images.getOrNull(1) ?: images.firstOrNull()
        return image?.uri
    }

val SimpleAlbum.largeImageUrl: String?
    get() = images.firstOrNull()?.uri
