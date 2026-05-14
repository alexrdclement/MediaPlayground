package com.alexrdclement.mediaplayground.data.album.fixtures

import com.alexrdclement.mediaplayground.data.album.local.LocalAudioAlbumDataStore
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStoreFixture
import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum

class LocalAlbumDataStoreFixture(
    private val trackDataStoreFixture: LocalTrackDataStoreFixture = LocalTrackDataStoreFixture(),
) {
    val localAudioAlbumDataStore: LocalAudioAlbumDataStore get() = trackDataStoreFixture.localAudioAlbumDataStore
    val localTrackDataStore get() = trackDataStoreFixture.localTrackDataStore

    suspend fun putTrack(albumTrack: AlbumTrack, simpleAlbum: SimpleAlbum) =
        trackDataStoreFixture.putTrack(albumTrack, simpleAlbum)

    suspend fun putTracks(albumTracks: List<AlbumTrack>, simpleAlbum: SimpleAlbum) =
        trackDataStoreFixture.putTracks(albumTracks, simpleAlbum)
}
