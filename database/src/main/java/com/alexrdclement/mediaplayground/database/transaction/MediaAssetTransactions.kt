package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaAssetSyncStateEntity
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState

context(scope: DatabaseTransactionScope)
suspend fun insertMediaAsset(
    mediaAsset: MediaAsset,
    syncState: MediaAssetSyncState = MediaAssetSyncState.Pending,
) {
    scope.mediaAssetDao.insert(mediaAsset)
    scope.mediaAssetSyncStateDao.insertIfAbsent(
        MediaAssetSyncStateEntity(mediaAssetId = mediaAsset.id, syncState = syncState)
    )
}

context(scope: DatabaseTransactionScope)
suspend fun insertMediaAssets(
    vararg mediaAssets: MediaAsset,
    syncState: MediaAssetSyncState = MediaAssetSyncState.Pending,
) {
    mediaAssets.forEach { insertMediaAsset(it, syncState) }
}

context(scope: DatabaseTransactionScope)
suspend fun deleteMediaAsset(id: String) {
    scope.mediaAssetDao.delete(id)
}
