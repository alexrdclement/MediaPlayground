package com.alexrdclement.mediaplayground.data.track

import android.net.Uri
import com.alexrdclement.mediaplayground.data.album.AudioAlbumRepositoryImpl
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStoreFixture
import com.alexrdclement.mediaplayground.media.mediaimport.AlbumTrackImporter
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.model.result.Result

class LocalTrackRepositoryFixture(
    val trackDataStoreFixture: LocalTrackDataStoreFixture = LocalTrackDataStoreFixture(),
) {
    private val trackImporter = object : AlbumTrackImporter {
        override suspend fun import(
            uri: Uri,
        ): Result<AlbumTrack, MediaImportError> =
            TODO("Not implemented in fixture")

        override suspend fun import(
            uris: List<Uri>,
        ): Map<Uri, Result<AlbumTrack, MediaImportError>> =
            TODO("Not implemented in fixture")
    }

    val trackRepository = TrackRepositoryImpl(
        localTrackDataStore = trackDataStoreFixture.localTrackDataStore,
        trackImporter = trackImporter,
    )

    val localAlbumDataStore get() = trackDataStoreFixture.localAudioAlbumDataStore

    val albumRepository = AudioAlbumRepositoryImpl(
        localAudioAlbumDataStore = trackDataStoreFixture.localAudioAlbumDataStore,
    )

    suspend fun putTrack(albumTrack: AlbumTrack, simpleAlbum: SimpleAlbum) {
        trackDataStoreFixture.putTrack(albumTrack, simpleAlbum)
    }

    suspend fun putTracks(albumTracks: List<AlbumTrack>, simpleAlbum: SimpleAlbum) {
        trackDataStoreFixture.putTracks(albumTracks, simpleAlbum)
    }

    suspend fun deleteTrack(albumTrack: AlbumTrack, simpleAlbum: SimpleAlbum) {
        trackDataStoreFixture.deleteTrack(albumTrack, simpleAlbum)
    }

    suspend fun deleteTracks(albumTracks: List<AlbumTrack>, simpleAlbum: SimpleAlbum) {
        trackDataStoreFixture.deleteTracks(albumTracks, simpleAlbum)
    }
}
