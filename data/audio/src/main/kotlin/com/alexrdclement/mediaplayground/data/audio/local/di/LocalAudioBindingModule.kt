package com.alexrdclement.mediaplayground.data.audio.local.di

import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface LocalAudioBindingModule {
    @Binds
    val LocalAudioRepositoryImpl.bind: LocalAudioRepository
}
