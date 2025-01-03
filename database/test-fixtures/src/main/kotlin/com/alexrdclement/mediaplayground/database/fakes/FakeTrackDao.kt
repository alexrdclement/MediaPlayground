package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeTrackDao : TrackDao {

    val tracks = MutableStateFlow(emptySet<Track>())

    override fun getTrackCountFlow(): Flow<Int> {
        return tracks.map { it.size }
    }

    override suspend fun getTracksForAlbum(albumId: String): List<Track> {
        return tracks.value.filter { it.albumId == albumId }
    }

    override suspend fun getTrack(id: String): Track? {
        return tracks.value.find { it.id == id }
    }

    override suspend fun insert(vararg track: Track) {
        for (newTrack in track) {
            val existingTrack = tracks.value.find { it.id == newTrack.id }
            if (existingTrack != null) {
                tracks.value -= existingTrack
            }
            tracks.value += newTrack
        }
    }

    override suspend fun delete(id: String) {
        val existingTrack = tracks.value.find { it.id == id } ?: return
        tracks.value -= existingTrack
    }
}
