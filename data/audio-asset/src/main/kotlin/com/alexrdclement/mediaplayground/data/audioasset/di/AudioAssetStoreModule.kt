package com.alexrdclement.mediaplayground.data.audioasset.di

import com.alexrdclement.mediaplayground.data.audioasset.AudioAssetStoreImpl
import com.alexrdclement.mediaplayground.media.store.AudioAssetStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface AudioAssetStoreModule {
    @Binds val AudioAssetStoreImpl.bind: AudioAssetStore
}
