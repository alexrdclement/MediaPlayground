package com.alexrdclement.mediaplayground.data.album.fixtures

import com.alexrdclement.mediaplayground.data.album.local.LocalAudioAlbumDataStore
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStoreFixture
import com.alexrdclement.mediaplayground.media.model.Track

class LocalAlbumDataStoreFixture(
    private val trackDataStoreFixture: LocalTrackDataStoreFixture = LocalTrackDataStoreFixture(),
) {
    val localAudioAlbumDataStore: LocalAudioAlbumDataStore get() = trackDataStoreFixture.localAudioAlbumDataStore
    val localTrackDataStore get() = trackDataStoreFixture.localTrackDataStore

    suspend fun putTrack(track: Track) = trackDataStoreFixture.putTrack(track)
    suspend fun putTracks(tracks: List<Track>) = trackDataStoreFixture.putTracks(tracks)
}
