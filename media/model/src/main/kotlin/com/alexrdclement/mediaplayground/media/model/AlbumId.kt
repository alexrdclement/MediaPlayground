package com.alexrdclement.mediaplayground.media.model

sealed interface AlbumId : AudioCollectionId {
    override val value: String
}
