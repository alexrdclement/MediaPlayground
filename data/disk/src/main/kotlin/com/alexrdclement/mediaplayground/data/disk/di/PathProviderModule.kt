package com.alexrdclement.mediaplayground.data.disk.di

import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.data.disk.PathProviderImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface PathProviderModule {
    @Binds
    val PathProviderImpl.bind: PathProvider
}
