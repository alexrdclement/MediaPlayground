package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList

sealed interface MediaCollectionId : MediaItemId {
    override val value: String
}

sealed interface MediaCollection<out T : MediaItem> {
    val id: MediaCollectionId
    val title: String
    val items: PersistentList<T>
    val images: PersistentList<Image>
    val isPlayable: Boolean
}
