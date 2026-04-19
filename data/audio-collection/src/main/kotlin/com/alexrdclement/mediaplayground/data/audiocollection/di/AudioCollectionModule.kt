package com.alexrdclement.mediaplayground.data.audiocollection.di

import com.alexrdclement.mediaplayground.data.audiocollection.AudioCollectionRepository
import com.alexrdclement.mediaplayground.data.audiocollection.AudioCollectionRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface AudioCollectionModule {
    @Binds val AudioCollectionRepositoryImpl.bind: AudioCollectionRepository
}
