package com.alexrdclement.mediaplayground.ui.model

import com.alexrdclement.mediaplayground.media.model.AudioItem

data class MediaItemUi(
    val mediaItem: AudioItem,
    val isLoaded: Boolean,
    val isPlayable: Boolean,
    val isPlaying: Boolean,
) {
    companion object {
        fun from(
            mediaItem: AudioItem,
            loadedMediaItem: AudioItem?,
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
