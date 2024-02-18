package com.alexrdclement.mediaplayground.data.audio.local

import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LocalAudioDataStore @Inject constructor() {

    // Temp until Room impl
    private val tracks = MutableStateFlow<List<Track>>(emptyList())

    fun putTrack(track: Track) {
        tracks.update {
            it.toMutableList().apply {
                add(track)
            }
        }
    }

    fun getTracks(): List<Track> {
        return tracks.value
    }

    fun getTracksFlow(): Flow<List<Track>> {
        return tracks
    }

    fun getTrack(trackId: TrackId): Track? {
        return tracks.value.firstOrNull { it.id == trackId }
    }
}
