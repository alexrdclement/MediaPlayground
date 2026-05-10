package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList

sealed interface MediaCollectionId : MediaItemId {
    override val value: String
}

sealed interface MediaCollection<out T : MediaItem> : MediaItem {
    override val id: MediaCollectionId
    val items: PersistentList<T>
    override val isPlayable: Boolean
        get() = items.any { it.isPlayable }
}
