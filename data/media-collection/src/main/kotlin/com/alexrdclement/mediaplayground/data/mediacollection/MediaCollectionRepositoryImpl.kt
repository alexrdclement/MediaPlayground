package com.alexrdclement.mediaplayground.data.mediacollection

import com.alexrdclement.mediaplayground.data.album.local.LocalAudioAlbumDataStore
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.MediaCollection
import com.alexrdclement.mediaplayground.media.model.MediaCollectionId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class MediaCollectionRepositoryImpl @Inject constructor(
    private val localAudioAlbumDataStore: LocalAudioAlbumDataStore,
) : MediaCollectionRepository {

    override fun getMediaCollectionFlow(id: MediaCollectionId): Flow<MediaCollection<*>?> =
        when (id) {
            is AlbumId -> localAudioAlbumDataStore.getAlbumFlow(id)
        }

    override suspend fun delete(id: MediaCollectionId) = when (id) {
        is AlbumId -> localAudioAlbumDataStore.delete(id)
    }
}
