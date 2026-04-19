package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.MediaAsset

context(scope: DatabaseTransactionScope)
suspend fun insertMediaAsset(mediaAsset: MediaAsset) {
    scope.mediaAssetDao.insert(mediaAsset)
}

context(scope: DatabaseTransactionScope)
suspend fun deleteMediaAsset(id: String) {
    scope.mediaAssetDao.delete(id)
}
