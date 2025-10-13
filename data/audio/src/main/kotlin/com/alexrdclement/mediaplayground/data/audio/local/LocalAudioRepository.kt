package com.alexrdclement.mediaplayground.data.audio.local

import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.flow.Flow

interface LocalAudioRepository {
    sealed class Failure {
        data object TrackNotFound : Failure()
        data object AlbumNotFound : Failure()
    }

    fun importTracksFromDisk(uris: List<Uri>): Flow<Map<Uri, MediaImportState>>
    fun getTrackImportProgress(): Flow<Map<Uri, MediaImportState>>
    fun getTrackCountFlow(): Flow<Int>
    fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>>
    suspend fun getTrack(id: TrackId): Result<Track?, Failure>
    fun getAlbumCountFlow(): Flow<Int>
    fun getAlbumPagingData(config: PagingConfig): Flow<PagingData<Album>>
    suspend fun getAlbum(id: AlbumId): Result<Album?, Failure>
}
