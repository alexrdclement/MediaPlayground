package com.alexrdclement.mediaplayground.model.audio

import kotlinx.serialization.Serializable

@Serializable
data class SimpleAlbum(
    val id: AlbumId,
    val name: String,
    val artists: List<SimpleArtist>,
    val images: List<Image>,
)

val SimpleAlbum.thumbnailImageUrl: String?
    get() {
        // Third image is typically too low-res
        val image = images.getOrNull(1) ?: images.firstOrNull()
        return image?.url
    }

val SimpleAlbum.largeImageUrl: String?
    get() = images.firstOrNull()?.url
