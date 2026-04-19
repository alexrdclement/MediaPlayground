package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.model.Track
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTrackDao : TrackDao {

    val tracks = MutableStateFlow(emptySet<Track>())

    override suspend fun getTracksForAlbum(albumId: String): List<Track> {
        return emptyList()
    }

    override suspend fun getTrack(id: String): Track? {
        return tracks.value.find { it.id == id }
    }

    override suspend fun insert(vararg track: Track) {
        for (newTrack in track) {
            if (tracks.value.any { it.id == newTrack.id }) continue
            tracks.value += newTrack
        }
    }

    override suspend fun update(track: Track) {
        val existing = tracks.value.find { it.id == track.id } ?: return
        tracks.value = tracks.value - existing + track
    }

    override suspend fun delete(id: String) {
        val existingTrack = tracks.value.find { it.id == id } ?: return
        tracks.value -= existingTrack
    }
}
