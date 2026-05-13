package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

sealed interface AudioItem : MediaItem {
    val artists: PersistentList<Artist> get() = persistentListOf()
}
