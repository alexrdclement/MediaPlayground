package com.alexrdclement.mediaplayground.media.model

sealed interface AudioCollectionId : MediaCollectionId {
    override val value: String
}

sealed interface AudioCollection<T : AudioItem> : MediaCollection<T>, AudioItem {
    override val id: AudioCollectionId
    override val duration: TimeUnit
}
