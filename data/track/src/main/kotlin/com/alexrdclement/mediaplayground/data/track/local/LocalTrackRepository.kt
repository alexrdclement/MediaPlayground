package com.alexrdclement.mediaplayground.data.track.local

import com.alexrdclement.mediaplayground.data.track.TrackRepository
import com.alexrdclement.mediaplayground.media.model.Track

interface LocalTrackRepository : TrackRepository, LocalTrackImporter {
    suspend fun putTrack(track: Track)
    suspend fun deleteTrack(track: Track)
}
