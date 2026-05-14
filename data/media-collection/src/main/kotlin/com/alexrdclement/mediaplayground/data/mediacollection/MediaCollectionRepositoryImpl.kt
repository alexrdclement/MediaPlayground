package com.alexrdclement.mediaplayground.data.mediacollection

import com.alexrdclement.mediaplayground.data.album.local.LocalAudioAlbumDataStore
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStore
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.MediaCollection
import com.alexrdclement.mediaplayground.media.model.MediaCollectionId
import com.alexrdclement.mediaplayground.media.model.TrackId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class MediaCollectionRepositoryImpl @Inject constructor(
    private val localAudioAlbumDataStore: LocalAudioAlbumDataStore,
    private val localTrackDataStore: LocalTrackDataStore,
) : MediaCollectionRepository {

    override fun getMediaCollectionFlow(id: MediaCollectionId): Flow<MediaCollection<*>?> =
        when (id) {
            is AlbumId -> localAudioAlbumDataStore.getAlbumFlow(id)
            is TrackId -> localTrackDataStore.getTrackFlow(id)
        }

    override suspend fun delete(id: MediaCollectionId) = when (id) {
        is AlbumId -> localAudioAlbumDataStore.delete(id)
        is TrackId -> localTrackDataStore.delete(id)
    }
}
