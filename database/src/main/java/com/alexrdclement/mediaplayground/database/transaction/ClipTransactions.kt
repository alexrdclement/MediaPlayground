package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AudioFile
import com.alexrdclement.mediaplayground.database.model.Clip

context(scope: DatabaseTransactionScope)
suspend fun insertClip(
    clip: Clip,
    audioFile: AudioFile,
) {
    insertAudioFile(audioFile)
    scope.clipDao.insert(clip)
}

context(scope: DatabaseTransactionScope)
suspend fun updateClip(
    id: String,
    title: String,
) {
    val clip = scope.clipDao.getClip(id) ?: error("Clip $id not found")
    scope.clipDao.update(
        clip = clip.copy(title = title),
    )
}

context(scope: DatabaseTransactionScope)
suspend fun deleteClip(id: String) {
    scope.clipDao.delete(id)
}
