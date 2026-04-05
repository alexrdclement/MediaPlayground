package com.alexrdclement.mediaplayground.data.track.local

import android.net.Uri
import com.alexrdclement.mediaplayground.data.track.TrackRepository
import com.alexrdclement.mediaplayground.media.model.Track
import kotlinx.coroutines.flow.Flow

interface LocalTrackRepository : TrackRepository {
    fun importTracksFromDisk(uris: List<Uri>): Flow<Map<Uri, TrackImportState>>
    fun getTrackImportProgress(): Flow<Map<Uri, TrackImportState>>
    suspend fun putTrack(track: Track)
    suspend fun deleteTrack(track: Track)
}
