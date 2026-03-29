package com.alexrdclement.mediaplayground.data.image.di

import com.alexrdclement.mediaplayground.data.image.ImageRepository
import com.alexrdclement.mediaplayground.data.image.local.LocalImageRepository
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface ImageBindingModule {
    @Binds val LocalImageRepository.bind: ImageRepository
}
