package com.alexrdclement.mediaplayground.data.audiocollection

import com.alexrdclement.mediaplayground.media.model.AudioCollection
import com.alexrdclement.mediaplayground.media.model.AudioCollectionId
import kotlinx.coroutines.flow.Flow

interface AudioCollectionRepository {
    fun getAudioCollectionFlow(id: AudioCollectionId): Flow<AudioCollection<*>?>
    suspend fun delete(id: AudioCollectionId)
}
