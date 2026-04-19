package com.alexrdclement.mediaplayground.data.audiocollection

import com.alexrdclement.mediaplayground.data.album.local.LocalAudioAlbumDataStore
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.AudioCollection
import com.alexrdclement.mediaplayground.media.model.AudioCollectionId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class AudioCollectionRepositoryImpl @Inject constructor(
    private val localAudioAlbumDataStore: LocalAudioAlbumDataStore,
) : AudioCollectionRepository {

    override fun getAudioCollectionFlow(id: AudioCollectionId): Flow<AudioCollection<*>?> =
        when (id) {
            is AlbumId -> localAudioAlbumDataStore.getAlbumFlow(id)
        }

    override suspend fun delete(id: AudioCollectionId) = when (id) {
        is AlbumId -> localAudioAlbumDataStore.delete(id)
    }
}
