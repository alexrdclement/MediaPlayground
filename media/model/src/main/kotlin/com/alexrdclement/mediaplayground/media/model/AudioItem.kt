package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList

sealed interface AudioItem : MediaItem {
    val title: String
    val artists: PersistentList<Artist>
    val images: PersistentList<Image>
    val isPlayable: Boolean
    val duration: TimeUnit
}

val AudioItem.thumbnailImageUri: MediaAssetUri?
    get() {
        // Third image is typically too low-res
        val image = images.getOrNull(1) ?: images.firstOrNull()
        return image?.uri
    }

val AudioItem.largeImageUri: MediaAssetUri?
    get() = images.firstOrNull()?.uri
