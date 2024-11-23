package com.alexrdclement.mediaplayground.data.audio.local

import com.alexrdclement.mediaplayground.data.audio.local.mapper.toTrack
import com.alexrdclement.mediaplayground.data.audio.local.mapper.toTrackEntity
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalAudioDataStore @Inject constructor(
    private val trackDao: TrackDao,
) {
    fun clearTracks() {
        // TODO
    }

    suspend fun putTrack(track: Track) {
        trackDao.insert(track.toTrackEntity())
    }

    suspend fun getTracks(): List<Track> {
        return trackDao.getTracks().map { it.toTrack() }
    }

    fun getTracksFlow(): Flow<List<Track>> {
        return trackDao.getTracksFlow().map { trackEntities -> trackEntities.map { it.toTrack() } }
    }

    suspend fun getTrack(trackId: TrackId): Track? {
        return trackDao.getTrack(trackId.value)?.toTrack()
    }
}
