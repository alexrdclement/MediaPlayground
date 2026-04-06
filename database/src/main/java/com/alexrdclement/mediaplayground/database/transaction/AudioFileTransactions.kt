package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.AudioFile

context(scope: DatabaseTransactionScope)
suspend fun insertAudioFile(audioFile: AudioFile) {
    scope.audioFileDao.insert(audioFile)
}

context(scope: DatabaseTransactionScope)
suspend fun deleteAudioFile(id: String) {
    scope.audioFileDao.delete(id)
}
