package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.Clip
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import kotlin.time.Clock

context(scope: DatabaseTransactionScope)
suspend fun insertClip(
    clip: Clip,
    mediaAsset: MediaAsset,
    audioAsset: AudioAsset,
) {
    insertAudioAsset(mediaAsset, audioAsset)
    scope.clipDao.insert(clip)
}

context(scope: DatabaseTransactionScope)
suspend fun updateClip(
    id: String,
    title: String,
) {
    val clip = scope.clipDao.getClip(id) ?: error("Clip $id not found")
    scope.clipDao.update(
        clip = clip.copy(title = title, modifiedAt = Clock.System.now()),
    )
}

context(scope: DatabaseTransactionScope)
suspend fun deleteClip(id: String) {
    scope.clipDao.delete(id)
}
