package com.alexrdclement.mediaplayground.data.disk.di

import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.data.disk.PathProviderImpl
import com.alexrdclement.mediaplayground.media.store.PathProvider as MediaStorePathProvider
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface PathProviderModule {
    @Binds
    val PathProviderImpl.bind: PathProvider

    @Binds
    val PathProviderImpl.bindMediaStore: MediaStorePathProvider
}
