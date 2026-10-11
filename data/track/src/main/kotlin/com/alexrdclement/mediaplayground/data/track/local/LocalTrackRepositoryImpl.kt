package com.alexrdclement.mediaplayground.data.track.local

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumRepository
import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistRepository
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class LocalTrackRepositoryImpl @Inject constructor(
    pathProvider: PathProvider,
    mediaImporter: MediaImporter,
    private val localTrackDataStore: LocalTrackDataStore,
    localAlbumRepository: LocalAlbumRepository,
    localArtistRepository: LocalArtistRepository,
) : LocalTrackRepository,
    LocalTrackImporter by LocalTrackImporterImpl(
        pathProvider = pathProvider,
        mediaImporter = mediaImporter,
        localTrackDataStore = localTrackDataStore,
        localAlbumRepository = localAlbumRepository,
        localArtistRepository = localArtistRepository,
    ) {

    override fun getTrackFlow(id: TrackId): Flow<Track?> = localTrackDataStore.getTrackFlow(id)

    override suspend fun getTrack(id: TrackId): Track? = localTrackDataStore.getTrack(id)

    override fun getTrackCountFlow(): Flow<Int> = localTrackDataStore.getTrackCountFlow()

    override fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>> =
        localTrackDataStore.getTrackPagingData(config)

    override suspend fun updateTrackTitle(
        id: TrackId,
        title: String,
    ) = localTrackDataStore.updateTrackTitle(id, title)

    override suspend fun updateTrackNumber(
        id: TrackId,
        trackNumber: Int?,
    ) = localTrackDataStore.updateTrackNumber(id, trackNumber)

    override suspend fun updateTrackNotes(
        id: TrackId,
        notes: String?,
    ) = localTrackDataStore.updateTrackNotes(id, notes)

    override suspend fun deleteTrack(id: TrackId) {
        val track = localTrackDataStore.getTrack(id) ?: return
        localTrackDataStore.deleteTrack(track)
    }

    override suspend fun putTrack(track: Track) = localTrackDataStore.putTrack(track)

    override suspend fun deleteTrack(track: Track) = localTrackDataStore.deleteTrack(track)
}
