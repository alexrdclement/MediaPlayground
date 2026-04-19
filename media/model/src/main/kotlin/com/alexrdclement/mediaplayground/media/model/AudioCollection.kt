package com.alexrdclement.mediaplayground.media.model

sealed interface AudioCollectionId : MediaCollectionId {
    override val value: String
}

sealed interface AudioCollection<T : AudioItem> : MediaCollection<T> {
    override val id: AudioCollectionId
    val duration: TimeUnit
}
