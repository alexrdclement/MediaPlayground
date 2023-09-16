package com.alexrdclement.ui.shared.model

import com.alexrdclement.mediaplayground.model.audio.MediaItem

data class MediaItemUi(
    val mediaItem: MediaItem,
    val isPlaying: Boolean,
)
