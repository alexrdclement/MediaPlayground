package com.alexrdclement.mediaplayground.data.track

import android.net.Uri
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.data.album.AlbumRepositoryImpl
import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStoreFixture
import com.alexrdclement.mediaplayground.media.mediaimport.TrackImporter
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

class LocalTrackRepositoryFixture(
    val pathProvider: FakePathProvider = FakePathProvider(),
    val mediaImporterFixture: MediaImporterFixture = MediaImporterFixture(),
    val trackDataStoreFixture: LocalTrackDataStoreFixture = LocalTrackDataStoreFixture(
        pathProvider = pathProvider,
    ),
) {
    private val trackImporter = object : TrackImporter {
        override suspend fun importTrackFromDisk(
            uri: Uri,
            getImportDir: (AlbumId) -> Path,
            getImagePath: (ImageId, extension: String) -> Path,
        ): Result<Track, MediaImportError> = TODO("Not implemented in fixture")

        override suspend fun importTracksFromDisk(
            uris: List<Uri>,
            getImportDir: (AlbumId) -> Path,
            getImagePath: (ImageId, extension: String) -> Path,
        ): Map<Uri, Result<Track, MediaImportError>> = TODO("Not implemented in fixture")
    }

    val trackRepository = TrackRepositoryImpl(
        pathProvider = pathProvider,
        mediaImporter = mediaImporterFixture.mediaImporter,
        localTrackDataStore = trackDataStoreFixture.localTrackDataStore,
        trackImporter = trackImporter,
    )

    val localAlbumDataStore get() = trackDataStoreFixture.localAlbumDataStore

    val albumRepository = AlbumRepositoryImpl(
        localAlbumDataStore = trackDataStoreFixture.localAlbumDataStore,
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
