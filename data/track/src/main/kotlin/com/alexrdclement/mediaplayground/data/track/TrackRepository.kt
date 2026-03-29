package com.alexrdclement.mediaplayground.data.track

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.audio.Track
import com.alexrdclement.mediaplayground.media.model.audio.TrackId
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getTrackFlow(id: TrackId): Flow<Track?>
    suspend fun getTrack(id: TrackId): Track?
    fun getTrackCountFlow(): Flow<Int>
    fun getTrackPagingData(config: PagingConfig): Flow<PagingData<Track>>
    suspend fun updateTrackTitle(id: TrackId, title: String)
    suspend fun updateTrackNumber(id: TrackId, trackNumber: Int?)
}
