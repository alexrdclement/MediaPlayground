package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.TrackClipDao
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef

class FakeTrackClipDao : TrackClipDao {

    val trackClips = mutableListOf<TrackClipCrossRef>()

    override suspend fun insert(vararg crossRef: TrackClipCrossRef) {
        for (ref in crossRef) {
            if (trackClips.none { it.trackId == ref.trackId && it.clipId == ref.clipId }) {
                trackClips.add(ref)
            }
        }
    }

    override suspend fun getClipIdsForTrack(trackId: String): List<String> {
        return trackClips.filter { it.trackId == trackId }.map { it.clipId }
    }

    override suspend fun getTrackIdsForClip(clipId: String): List<String> {
        return trackClips.filter { it.clipId == clipId }.map { it.trackId }
    }

    override suspend fun deleteForTrack(trackId: String) {
        trackClips.removeAll { it.trackId == trackId }
    }
}
