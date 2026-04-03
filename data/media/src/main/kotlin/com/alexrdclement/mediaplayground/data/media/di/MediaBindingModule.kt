package com.alexrdclement.mediaplayground.data.media.di

import com.alexrdclement.mediaplayground.data.media.MediaItemRepositoryImpl
import com.alexrdclement.mediaplayground.media.engine.MediaItemRepository
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaBindingModule {
    @Binds val MediaItemRepositoryImpl.bind: MediaItemRepository
}
