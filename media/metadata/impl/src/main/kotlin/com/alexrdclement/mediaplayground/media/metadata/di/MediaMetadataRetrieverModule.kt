package com.alexrdclement.mediaplayground.media.metadata.di

import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetrieverImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaMetadataRetrieverModule {
    @Binds
    val MediaMetadataRetrieverImpl.bind: MediaMetadataRetriever
}
