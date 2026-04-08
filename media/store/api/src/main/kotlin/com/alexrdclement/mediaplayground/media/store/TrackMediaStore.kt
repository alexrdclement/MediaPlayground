package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.coroutines.flow.Flow

interface TrackMediaStore {
    fun getTrackFlow(id: TrackId): Flow<Track?>

    suspend fun getByClipId(clipId: ClipId): Track?

    context(scope: MediaStoreTransactionScope)
    suspend fun put(track: Track)

    context(scope: MediaStoreTransactionScope)
    suspend fun delete(trackId: TrackId)
}
