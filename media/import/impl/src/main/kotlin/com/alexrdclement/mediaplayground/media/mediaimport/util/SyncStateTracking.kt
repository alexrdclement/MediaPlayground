package com.alexrdclement.mediaplayground.media.mediaimport.util

import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

internal suspend fun <T, E> MediaAssetSyncStateStore.runTracked(
    id: MediaAssetId,
    transactionRunner: MediaStoreTransactionRunner,
    block: suspend MediaStoreTransactionScope.() -> Result<T, E>,
): Result<T, E> {
    upsert(id, MediaAssetSyncState.Syncing)
    var finalState = MediaAssetSyncState.Failed
    return try {
        val result = transactionRunner.run(block)
        finalState = if (result is Result.Success) MediaAssetSyncState.Synced else MediaAssetSyncState.Failed
        result
    } finally {
        withContext(NonCancellable) {
            upsert(id, finalState)
        }
    }
}
