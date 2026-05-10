package com.alexrdclement.mediaplayground.media.model

sealed interface MediaItemId {
    val value: String
}

sealed interface MediaItem {
    val id: MediaItemId
}
