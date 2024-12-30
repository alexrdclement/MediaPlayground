package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeTrackDao : TrackDao {

    val tracks = MutableStateFlow(emptySet<Track>())

    override suspend fun getTracks(albumId: String): List<Track> {
        return tracks.value.filter { it.albumId == albumId }
    }

    override fun getTracksFlow(albumId: String): Flow<List<Track>> {
        return tracks.map { it.filter { it.albumId == albumId } }
    }

    override suspend fun getTrack(id: String): Track? {
        return tracks.value.find { it.id == id }
    }

    override suspend fun insert(track: Track) {
        val existingTrack = tracks.value.find { it.id == track.id }
        val newTracks = tracks.value.toMutableSet()
        if (existingTrack != null) {
            newTracks -= existingTrack
        }
        tracks.value = newTracks + track
    }

    override suspend fun delete(id: String) {
        val existingTrack = tracks.value.find { it.id == id } ?: return
        tracks.value -= existingTrack
    }
}
