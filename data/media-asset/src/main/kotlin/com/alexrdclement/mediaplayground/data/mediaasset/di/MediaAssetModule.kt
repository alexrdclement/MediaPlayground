package com.alexrdclement.mediaplayground.data.mediaasset.di

import com.alexrdclement.mediaplayground.data.mediaasset.MediaAssetRepository
import com.alexrdclement.mediaplayground.data.mediaasset.MediaAssetRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaAssetModule {
    @Binds val MediaAssetRepositoryImpl.bind: MediaAssetRepository
}
