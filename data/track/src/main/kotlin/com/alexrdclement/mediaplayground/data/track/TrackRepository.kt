package com.alexrdclement.mediaplayground.data.track

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getTrackFlow(id: TrackId): Flow<Track?>
    suspend fun getTrack(id: TrackId): Track?
    fun getTrackCountFlow(): Flow<Int>
    fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>>
    suspend fun updateTrackTitle(id: TrackId, title: String)
    suspend fun updateTrackNumber(id: TrackId, trackNumber: Int?)
    suspend fun updateTrackNotes(id: TrackId, notes: String?)
    suspend fun deleteTrack(id: TrackId)
}
