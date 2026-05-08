package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef
import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.Clip
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaCollection
import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.database.model.MediaItemType
import com.alexrdclement.mediaplayground.database.model.Track
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import kotlin.time.Clock

context(scope: DatabaseTransactionScope)
suspend fun insertTrack(
    mediaCollection: MediaCollection,
    track: Track,
    albumTrackCrossRef: AlbumTrackCrossRef,
    clips: List<Triple<Clip, MediaAsset, AudioAsset>>,
    trackClipCrossRefs: List<TrackClipCrossRef>,
) = with(scope) {
    for ((clip, mediaAsset, audioAsset) in clips) {
        insertClip(
            clip = clip,
            mediaAsset = mediaAsset,
            audioAsset = audioAsset,
        )
    }
    mediaItemDao.insert(MediaItem(id = mediaCollection.id, itemType = MediaItemType.COLLECTION))
    mediaCollectionDao.insert(mediaCollection)
    trackDao.insert(track)
    albumTrackDao.insert(albumTrackCrossRef)
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
    val mediaCollection = scope.mediaCollectionDao.getMediaCollection(id)
        ?: error("MediaCollection $id not found")
    scope.mediaCollectionDao.update(mediaCollection.copy(title = title, modifiedAt = Clock.System.now()))
}

context(scope: DatabaseTransactionScope)
suspend fun deleteTrack(
    id: String,
    deleteOrphanedClips: Boolean = true,
) {
    val orphanedClipIds = if (deleteOrphanedClips) {
        val clipIds = scope.trackClipDao.getClipIdsForTrack(id)
        clipIds.filter { clipId ->
            scope.trackClipDao.getTrackIdsForClip(clipId) == listOf(id)
        }
    } else {
        emptyList()
    }
    scope.trackClipDao.deleteForTrack(id)
    scope.mediaItemDao.delete(id)
    for (clipId in orphanedClipIds) {
        deleteClip(clipId)
    }
}
