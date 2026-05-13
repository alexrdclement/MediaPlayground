package com.alexrdclement.mediaplayground.data.track

import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStore
import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.TrackId
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.TrackMediaStore
import dev.zacsweers.metro.Inject

class TrackMediaStoreImpl @Inject constructor(
    private val localTrackDataStore: LocalTrackDataStore,
) : TrackMediaStore {

    override fun getTrackFlow(id: TrackId) =
        localTrackDataStore.getTrackFlow(id)

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(albumTrack: AlbumTrack) =
        localTrackDataStore.put(albumTrack)

    context(scope: MediaStoreTransactionScope)
    override suspend fun delete(trackId: TrackId) =
        localTrackDataStore.delete(trackId)
}
