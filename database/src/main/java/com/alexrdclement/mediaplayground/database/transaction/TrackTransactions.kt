package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef
import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.AudioClip
import com.alexrdclement.mediaplayground.database.model.Clip
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaCollection
import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.database.model.MediaItemType
import com.alexrdclement.mediaplayground.database.model.Track
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef
import com.alexrdclement.mediaplayground.media.model.deletion.DeleteTrackPolicy
import kotlin.time.Clock

data class ClipData(
    val clip: Clip,
    val audioClip: AudioClip,
    val mediaAsset: MediaAsset,
    val audioAsset: AudioAsset,
    val artistIds: Set<String> = emptySet(),
    val imageIds: Set<String> = emptySet(),
)

context(scope: DatabaseTransactionScope)
suspend fun insertTrack(
    mediaCollection: MediaCollection,
    track: Track,
    albumTrackCrossRefs: List<AlbumTrackCrossRef>,
    clips: List<ClipData>,
    trackClipCrossRefs: List<TrackClipCrossRef>,
) = with(scope) {
    for ((clip, audioClip, mediaAsset, audioAsset, artistIds, imageIds) in clips) {
        insertClip(
            clip = clip,
            audioClip = audioClip,
            mediaAsset = mediaAsset,
            audioAsset = audioAsset,
            artistIds = artistIds,
            imageIds = imageIds,
        )
    }
    mediaItemDao.insert(MediaItem(id = mediaCollection.id, itemType = MediaItemType.COLLECTION))
    mediaCollectionDao.insert(mediaCollection)
    trackDao.insert(track)
    for (crossRef in albumTrackCrossRefs) {
        albumTrackDao.insert(crossRef)
    }
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
    policy: DeleteTrackPolicy = DeleteTrackPolicy(),
) {
    val orphanedClipIds = if (policy.deleteOrphanedClips) {
        val clipIds = scope.trackClipDao.getClipIdsForTrack(id)
        clipIds.filter { clipId ->
            scope.trackClipDao.getTrackIdsForClip(clipId) == listOf(id)
        }
    } else {
        emptyList()
    }
    scope.trackClipDao.deleteForTrack(id)
    scope.trackDao.delete(id)
    scope.mediaCollectionDao.delete(id)
    scope.mediaItemDao.delete(id)
    for (clipId in orphanedClipIds) {
        deleteClip(clipId, policy.clipPolicy)
    }
}
