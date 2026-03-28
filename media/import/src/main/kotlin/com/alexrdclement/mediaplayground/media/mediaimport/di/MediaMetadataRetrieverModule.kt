package com.alexrdclement.mediaplayground.media.mediaimport.di

import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetrieverImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaMetadataRetrieverModule {
    @Binds
    val MediaMetadataRetrieverImpl.bind: MediaMetadataRetriever
}
