package com.alexrdclement.mediaplayground.model.audio

import kotlin.time.Duration

sealed interface MediaItemId {
    val value: String
}

sealed interface MediaItem {
    val id: MediaItemId
    val title: String
    val artists: List<SimpleArtist>
    val images: List<Image>
    val isPlayable: Boolean
    val duration: Duration
    val source: Source
}

val MediaItem.thumbnailImageUrl: String?
    get() {
        // Third image is typically too low-res
        val image = images.getOrNull(1) ?: images.firstOrNull()
        return image?.uri
    }

val MediaItem.largeImageUrl: String?
    get() = images.firstOrNull()?.uri
