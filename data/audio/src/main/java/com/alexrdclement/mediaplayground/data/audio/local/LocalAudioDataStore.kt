package com.alexrdclement.mediaplayground.data.audio.local

import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAudioDataStore @Inject constructor() {

    // Temp until DB impl
    private val tracks = MutableStateFlow<List<Track>>(emptyList())

    fun putTrack(track: Track) {
        tracks.update {
            it.toMutableList().apply {
                add(0, track)
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
