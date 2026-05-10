package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList

sealed interface AudioItem : MediaItem {
    val artists: PersistentList<Artist>
}
