package com.alexrdclement.mediaplayground.data.audio.local.di

import com.alexrdclement.mediaplayground.data.audio.local.PathProvider
import com.alexrdclement.mediaplayground.data.audio.local.PathProviderImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface PathProviderModule {
    @Binds
    val PathProviderImpl.bind: PathProvider
}
