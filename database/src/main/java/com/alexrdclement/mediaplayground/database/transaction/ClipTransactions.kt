package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.AudioClip
import com.alexrdclement.mediaplayground.database.model.Clip
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.deletion.DeleteClipPolicy
import kotlin.time.Clock

context(scope: DatabaseTransactionScope)
suspend fun insertClip(
    clipMediaItem: MediaItem,
    clip: Clip,
    audioClip: AudioClip,
    assetMediaItem: MediaItem,
    mediaAsset: MediaAsset,
    audioAsset: AudioAsset,
    artistIds: Set<String> = emptySet(),
    imageIds: Set<String> = emptySet(),
) {
    insertAudioAsset(assetMediaItem, mediaAsset, audioAsset, artistIds, imageIds)
    scope.mediaItemDao.insert(clipMediaItem)
    scope.clipDao.insert(clip)
    scope.audioClipDao.insert(audioClip)
}

context(scope: DatabaseTransactionScope)
suspend fun updateClip(
    id: String,
    title: String,
) {
    val mediaItem = scope.mediaItemDao.getMediaItem(id) ?: error("MediaItem $id not found")
    scope.mediaItemDao.update(mediaItem.copy(title = title, modifiedAt = Clock.System.now()))
}

context(scope: DatabaseTransactionScope)
suspend fun deleteClip(id: String, policy: DeleteClipPolicy = DeleteClipPolicy()) {
    val assetId = if (policy.deleteOrphanedAssets) {
        scope.clipDao.getClip(id)?.assetId
    } else {
        null
    }
    val assetIsOrphaned = assetId != null &&
        scope.clipDao.getClipIdsByAssetId(assetId) == listOf(id)

    scope.trackClipDao.deleteForClip(id)
    scope.clipDao.delete(id)
    scope.mediaItemDao.delete(id)

    if (assetIsOrphaned && assetId != null) {
        deleteAudioAsset(assetId)
    }
}

