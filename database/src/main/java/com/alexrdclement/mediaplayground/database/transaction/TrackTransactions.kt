package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AudioFile
import com.alexrdclement.mediaplayground.database.model.Clip
import com.alexrdclement.mediaplayground.database.model.Track
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef

context(scope: DatabaseTransactionScope)
suspend fun insertTrack(
    track: Track,
    clips: List<Pair<Clip, AudioFile>>,
    trackClipCrossRefs: List<TrackClipCrossRef>,
) {
    for ((clip, audioFile) in clips) {
        insertClip(clip, audioFile)
    }
    scope.trackDao.insert(track)
    for (crossRef in trackClipCrossRefs) {
        insertTrackClipCrossRef(crossRef)
    }
}

context(scope: DatabaseTransactionScope)
suspend fun insertTrackClipCrossRef(crossRef: TrackClipCrossRef) {
    scope.trackClipDao.insert(crossRef)
}

context(scope: DatabaseTransactionScope)
suspend fun updateTrack(id: String, title: String) {
    val track = scope.trackDao.getTrack(id) ?: error("Track $id not found")
    scope.trackDao.update(track.copy(title = title))
}

context(scope: DatabaseTransactionScope)
suspend fun deleteTrack(id: String) {
    scope.trackDao.delete(id)
}
