package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.AudioAssetImageDao
import com.alexrdclement.mediaplayground.database.model.AudioAssetImageCrossRef

class FakeAudioAssetImageDao : AudioAssetImageDao {

    val audioAssetImages = mutableSetOf<AudioAssetImageCrossRef>()

    override suspend fun insert(vararg crossRef: AudioAssetImageCrossRef) {
        audioAssetImages.addAll(crossRef)
    }

    override suspend fun deleteForAudioAsset(audioAssetId: String) {
        audioAssetImages.removeAll { it.audioAssetId == audioAssetId }
    }
}
