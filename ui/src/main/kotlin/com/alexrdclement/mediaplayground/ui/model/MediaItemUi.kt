package com.alexrdclement.mediaplayground.ui.model

import com.alexrdclement.mediaplayground.model.audio.MediaItem

// TODO: MediaItem contains collections which are unstable. Use kotlinx.collections.immutable variants.
data class MediaItemUi(
    val mediaItem: MediaItem,
    val isLoaded: Boolean,
    val isPlayable: Boolean,
    val isPlaying: Boolean,
) {
    companion object {
        fun from(
            mediaItem: MediaItem,
            loadedMediaItem: MediaItem?,
            isPlaying: Boolean,
        ): MediaItemUi {
            return MediaItemUi(
                mediaItem = mediaItem,
                isLoaded = mediaItem.id == loadedMediaItem?.id,
                isPlayable = mediaItem.isPlayable,
                isPlaying = isPlaying && mediaItem.id == loadedMediaItem?.id,
            )
        }
    }
}
