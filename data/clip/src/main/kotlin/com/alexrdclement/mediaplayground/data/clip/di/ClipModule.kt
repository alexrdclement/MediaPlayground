package com.alexrdclement.mediaplayground.data.clip.di

import com.alexrdclement.mediaplayground.data.clip.ClipRepository
import com.alexrdclement.mediaplayground.data.clip.ClipRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface ClipModule {
    @Binds val ClipRepositoryImpl.bindRepository: ClipRepository
}
