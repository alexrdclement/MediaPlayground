package com.alexrdclement.mediaplayground.data.audioasset.di

import com.alexrdclement.mediaplayground.data.audioasset.AudioAssetRepository
import com.alexrdclement.mediaplayground.data.audioasset.AudioAssetRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface AudioAssetModule {
    @Binds val AudioAssetRepositoryImpl.bind: AudioAssetRepository
}
