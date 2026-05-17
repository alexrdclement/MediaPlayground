package com.alexrdclement.mediaplayground.data.image.di

import com.alexrdclement.mediaplayground.data.image.ImageRepository
import com.alexrdclement.mediaplayground.data.image.ImageRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface ImageModule {
    @Binds val ImageRepositoryImpl.bind: ImageRepository
}
