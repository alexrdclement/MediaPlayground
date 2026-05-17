package com.alexrdclement.mediaplayground.data.image.di

import com.alexrdclement.mediaplayground.data.image.ImageMediaStoreImpl
import com.alexrdclement.mediaplayground.media.store.ImageMediaStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface ImageMediaStoreModule {
    @Binds val ImageMediaStoreImpl.bind: ImageMediaStore
}
