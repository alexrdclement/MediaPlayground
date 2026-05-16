package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.ImageAsset
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaItem

context(scope: DatabaseTransactionScope)
suspend fun insertImageAsset(mediaItem: MediaItem, mediaAsset: MediaAsset, image: ImageAsset) {
    insertMediaAsset(mediaItem, mediaAsset)
    scope.imageAssetDao.insert(image)
}

context(scope: DatabaseTransactionScope)
suspend fun insertImageAssets(vararg imageAssets: Triple<MediaItem, MediaAsset, ImageAsset>) {
    imageAssets.forEach { (mediaItem, mediaAsset, image) -> insertImageAsset(mediaItem, mediaAsset, image) }
}

context(scope: DatabaseTransactionScope)
suspend fun deleteImage(id: String) {
    scope.imageAssetDao.delete(id)
    deleteMediaAsset(id)
}
