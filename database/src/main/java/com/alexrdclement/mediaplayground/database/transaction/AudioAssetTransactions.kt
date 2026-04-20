package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.MediaAsset

context(scope: DatabaseTransactionScope)
suspend fun insertAudioAsset(mediaAsset: MediaAsset, audioAsset: AudioAsset) {
    insertMediaAsset(mediaAsset)
    scope.audioAssetDao.insert(audioAsset)
}

context(scope: DatabaseTransactionScope)
suspend fun deleteAudioAsset(id: String) {
    scope.audioAssetDao.delete(id)
    deleteMediaAsset(id)
}
