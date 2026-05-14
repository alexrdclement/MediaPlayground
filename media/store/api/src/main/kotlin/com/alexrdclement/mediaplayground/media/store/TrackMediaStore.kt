package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.coroutines.flow.Flow

interface TrackMediaStore {
    fun getTrackFlow(id: TrackId): Flow<Track?>

    context(scope: MediaStoreTransactionScope)
    suspend fun put(albumTrack: AlbumTrack)

    context(scope: MediaStoreTransactionScope)
    suspend fun delete(trackId: TrackId)
}
