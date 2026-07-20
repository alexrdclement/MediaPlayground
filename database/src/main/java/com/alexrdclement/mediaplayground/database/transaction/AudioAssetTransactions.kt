package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.AudioAssetArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.MediaItemImageCrossRef
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaItem

context(scope: DatabaseTransactionScope)
suspend fun insertAudioAsset(
    mediaItem: MediaItem,
    mediaAsset: MediaAsset,
    audioAsset: AudioAsset,
    artistIds: Set<String> = emptySet(),
    imageIds: Set<String> = emptySet(),
) {
    insertMediaAsset(mediaItem, mediaAsset)
    scope.audioAssetDao.insert(audioAsset)
    if (artistIds.isNotEmpty()) {
        val crossRefs = artistIds.map { AudioAssetArtistCrossRef(audioAsset.id, it) }.toTypedArray()
        scope.audioAssetArtistDao.insert(*crossRefs)
    }
    if (imageIds.isNotEmpty()) {
        val crossRefs = imageIds.map { MediaItemImageCrossRef(audioAsset.id, it) }.toTypedArray()
        scope.mediaItemImageDao.insert(*crossRefs)
    }
}

context(scope: DatabaseTransactionScope)
suspend fun deleteAudioAsset(id: String) {
    val clipIds = scope.clipDao.getClipIdsByAssetId(id)
    for (clipId in clipIds) {
        deleteClip(clipId)
    }
    scope.audioAssetArtistDao.deleteForAudioAsset(id)
    scope.mediaItemImageDao.deleteForItem(id)
    scope.audioAssetDao.delete(id)
    deleteMediaAsset(id)
}
