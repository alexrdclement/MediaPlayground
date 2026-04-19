package com.alexrdclement.mediaplayground.data.sync.di

import com.alexrdclement.mediaplayground.data.sync.LocalMediaAssetSyncStateDataStore
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface SyncModule {
    @Binds val LocalMediaAssetSyncStateDataStore.bind: MediaAssetSyncStateStore
}
