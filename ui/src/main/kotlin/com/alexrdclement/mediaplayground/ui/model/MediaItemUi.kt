package com.alexrdclement.mediaplayground.ui.model

import com.alexrdclement.mediaplayground.model.audio.MediaItem

// TODO: MediaItem contains collections which are unstable. Use kotlinx.collections.immutable variants.
data class MediaItemUi(
    val mediaItem: MediaItem,
    val isPlaying: Boolean,
)
