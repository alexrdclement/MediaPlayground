package com.alexrdclement.mediaplayground.data.track

import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStore
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.TrackMediaStore
import dev.zacsweers.metro.Inject

class TrackMediaStoreImpl @Inject constructor(
    private val localTrackDataStore: LocalTrackDataStore,
) : TrackMediaStore {

    override fun getTrackFlow(id: TrackId) =
        localTrackDataStore.getTrackFlow(id)

    override suspend fun getByClipId(clipId: ClipId) =
        localTrackDataStore.getByClipId(clipId)

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(track: Track) =
        localTrackDataStore.put(track)

    context(scope: MediaStoreTransactionScope)
    override suspend fun delete(trackId: TrackId) =
        localTrackDataStore.delete(trackId)
}
