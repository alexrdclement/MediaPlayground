package com.alexrdclement.mediaplayground.data.mediaasset.di

import com.alexrdclement.mediaplayground.data.mediaasset.MediaAssetStoreImpl
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaAssetStoreModule {
    @Binds val MediaAssetStoreImpl.bind: MediaAssetStore
}
