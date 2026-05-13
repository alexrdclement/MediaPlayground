package com.alexrdclement.mediaplayground.media.model

import kotlinx.collections.immutable.PersistentList

sealed interface AudioCollectionId : MediaCollectionId {
    override val value: String
}

sealed interface AudioCollection<T : AudioItem> : MediaCollection<T>, AudioItem {
    override val id: AudioCollectionId
    override val artists: PersistentList<Artist>
    override val duration: TimeUnit
        get() = items.map { it.duration }
            .reduceOrNull { a, b -> a + b } ?: TimeUnit.Samples(0L, 44100)
}
