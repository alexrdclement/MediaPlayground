package com.alexrdclement.mediaplayground.ui.model

import com.alexrdclement.mediaplayground.media.model.AudioItem
import com.alexrdclement.mediaplayground.media.model.MediaItem

data class MediaItemUi(
    val mediaItem: MediaItem,
    val subtitle: String,
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
                subtitle = when (mediaItem) {
                    is AudioItem -> mediaItem.artists.joinToString { it.name ?: "" }
                    else -> ""
                },
                isLoaded = mediaItem.id == loadedMediaItem?.id,
                isPlayable = mediaItem.isPlayable,
                isPlaying = isPlaying && mediaItem.id == loadedMediaItem?.id,
            )
        }
    }
}
