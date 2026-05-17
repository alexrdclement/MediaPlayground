package com.alexrdclement.mediaplayground.data.sync.di

import com.alexrdclement.mediaplayground.data.sync.LocalMediaAssetDataStore
import com.alexrdclement.mediaplayground.data.sync.LocalMediaAssetSyncStateDataStore
import com.alexrdclement.mediaplayground.data.sync.MediaStoreTransactionRunnerImpl
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface SyncModule {
    @Binds val LocalMediaAssetDataStore.bind: MediaAssetStore
    @Binds val LocalMediaAssetSyncStateDataStore.bind: MediaAssetSyncStateStore
    @Binds val MediaStoreTransactionRunnerImpl.bind: MediaStoreTransactionRunner
}
