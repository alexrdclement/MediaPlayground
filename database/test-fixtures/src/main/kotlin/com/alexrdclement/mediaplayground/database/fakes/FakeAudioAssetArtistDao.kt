package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.AudioAssetArtistDao
import com.alexrdclement.mediaplayground.database.model.AudioAssetArtistCrossRef

class FakeAudioAssetArtistDao : AudioAssetArtistDao {

    val audioAssetArtists = mutableSetOf<AudioAssetArtistCrossRef>()

    override suspend fun insert(vararg crossRef: AudioAssetArtistCrossRef) {
        audioAssetArtists.addAll(crossRef)
    }

    override suspend fun deleteForAudioAsset(audioAssetId: String) {
        audioAssetArtists.removeAll { it.audioAssetId == audioAssetId }
    }
}
