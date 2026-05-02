package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList

sealed interface MediaItemId {
    val value: String
}

sealed interface MediaItem {
    val id: MediaItemId
    val title: String
    val artists: PersistentList<Artist>
    val images: PersistentList<Image>
    val isPlayable: Boolean
    val duration: TimeUnit
}

val MediaItem.thumbnailImageUri: MediaAssetUri?
    get() {
        // Third image is typically too low-res
        val image = images.getOrNull(1) ?: images.firstOrNull()
        return image?.uri
    }

val MediaItem.largeImageUri: MediaAssetUri?
    get() = images.firstOrNull()?.uri
