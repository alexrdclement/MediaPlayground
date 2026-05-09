package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.ImageAsset
import com.alexrdclement.mediaplayground.database.model.MediaAsset

context(scope: DatabaseTransactionScope)
suspend fun insertImageAsset(mediaAsset: MediaAsset, image: ImageAsset) {
    insertMediaAsset(mediaAsset)
    scope.imageAssetDao.insert(image)
}

context(scope: DatabaseTransactionScope)
suspend fun insertImageAssets(vararg imageAssets: Pair<MediaAsset, ImageAsset>) {
    insertMediaAssets(*imageAssets.map { it.first }.toTypedArray())
    scope.imageAssetDao.insert(*imageAssets.map { it.second }.toTypedArray())
}

context(scope: DatabaseTransactionScope)
suspend fun deleteImage(id: String) {
    scope.imageAssetDao.delete(id)
    deleteMediaAsset(id)
}
