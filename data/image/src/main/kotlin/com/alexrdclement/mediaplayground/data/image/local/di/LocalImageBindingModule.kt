package com.alexrdclement.mediaplayground.data.image.local.di

import com.alexrdclement.mediaplayground.data.image.local.LocalImageRepository
import com.alexrdclement.mediaplayground.data.image.local.LocalImageRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface LocalImageBindingModule {
    @Binds val LocalImageRepositoryImpl.bind: LocalImageRepository
}
