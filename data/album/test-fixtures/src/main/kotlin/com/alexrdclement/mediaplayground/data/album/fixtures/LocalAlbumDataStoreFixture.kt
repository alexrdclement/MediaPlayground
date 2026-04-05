package com.alexrdclement.mediaplayground.data.album.fixtures

import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumDataStore
import com.alexrdclement.mediaplayground.data.track.fixtures.LocalTrackDataStoreFixture
import com.alexrdclement.mediaplayground.media.model.Track

class LocalAlbumDataStoreFixture(
    private val trackDataStoreFixture: LocalTrackDataStoreFixture = LocalTrackDataStoreFixture(),
) {
    val localAlbumDataStore: LocalAlbumDataStore get() = trackDataStoreFixture.localAlbumDataStore

    suspend fun putTrack(track: Track) = trackDataStoreFixture.putTrack(track)
    suspend fun putTracks(tracks: List<Track>) = trackDataStoreFixture.putTracks(tracks)
}
