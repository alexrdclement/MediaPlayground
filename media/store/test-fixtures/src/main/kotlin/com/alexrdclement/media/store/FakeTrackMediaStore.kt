package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.TrackMediaStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeTrackMediaStore : TrackMediaStore {

    private val tracksFlow = MutableStateFlow<Map<TrackId, Track>>(emptyMap())

    override fun getTrackFlow(id: TrackId): Flow<Track?> =
        tracksFlow.map { it[id] }

    override suspend fun getByClipId(clipId: ClipId): Track? =
        tracksFlow.value.values.find { track -> track.clips.any { it.clip.id == clipId } }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(track: Track) {
        tracksFlow.update { it + (track.id to track) }
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun delete(trackId: TrackId) {
        tracksFlow.update { it - trackId }
    }
}
