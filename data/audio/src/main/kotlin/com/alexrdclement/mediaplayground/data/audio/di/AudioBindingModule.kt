package com.alexrdclement.mediaplayground.data.audio.di

import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.data.audio.AudioRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface AudioBindingModule {
    @Binds
    val AudioRepositoryImpl.bind: AudioRepository
}
