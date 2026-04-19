package com.alexrdclement.mediaplayground.data.track

import android.net.Uri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.data.album.AudioAlbumRepositoryImpl
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStoreFixture
import com.alexrdclement.mediaplayground.media.mediaimport.TrackImporter
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AudioTrack
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.model.result.Result

class LocalTrackRepositoryFixture(
    val mediaImporterFixture: MediaImporterFixture = MediaImporterFixture(),
    val trackDataStoreFixture: LocalTrackDataStoreFixture = LocalTrackDataStoreFixture(),
) {
    private val trackImporter = object : TrackImporter {
        override suspend fun import(
            uri: Uri,
        ): Result<AudioTrack, MediaImportError> =
            TODO("Not implemented in fixture")

        override suspend fun import(
            uris: List<Uri>,
        ): Map<Uri, Result<AudioTrack, MediaImportError>> =
            TODO("Not implemented in fixture")
    }

    val trackRepository = TrackRepositoryImpl(
        mediaImporter = mediaImporterFixture.imageImporter,
        localTrackDataStore = trackDataStoreFixture.localTrackDataStore,
        trackImporter = trackImporter,
    )

    val localAlbumDataStore get() = trackDataStoreFixture.localAudioAlbumDataStore

    val albumRepository = AudioAlbumRepositoryImpl(
        localAudioAlbumDataStore = trackDataStoreFixture.localAudioAlbumDataStore,
    )

    suspend fun putTrack(track: Track) {
        trackDataStoreFixture.putTrack(track)
    }

    suspend fun putTracks(tracks: List<Track>) {
        trackDataStoreFixture.putTracks(tracks)
    }

    suspend fun deleteTrack(track: Track) {
        trackDataStoreFixture.deleteTrack(track)
    }

    suspend fun deleteTracks(tracks: List<Track>) {
        trackDataStoreFixture.deleteTracks(tracks)
    }
}
