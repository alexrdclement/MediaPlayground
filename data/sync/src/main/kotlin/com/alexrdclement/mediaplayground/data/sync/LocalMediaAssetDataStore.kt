package com.alexrdclement.mediaplayground.data.sync

import com.alexrdclement.mediaplayground.database.mapping.toMediaAssetRecord
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.insertMediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import dev.zacsweers.metro.Inject

@Inject
class LocalMediaAssetDataStore(
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) : MediaAssetStore {
    override suspend fun put(asset: MediaAsset) = databaseTransactionRunner.run {
        insertMediaAsset(asset.toMediaAssetRecord())
    }
}
