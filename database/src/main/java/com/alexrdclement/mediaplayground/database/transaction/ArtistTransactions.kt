package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.Artist

context(scope: DatabaseTransactionScope)
suspend fun insertArtist(artist: Artist) {
    scope.artistDao.insert(artist)
}

context(scope: DatabaseTransactionScope)
suspend fun updateArtist(artist: Artist) {
    scope.artistDao.update(artist)
}

context(scope: DatabaseTransactionScope)
suspend fun deleteArtist(id: String) {
    scope.artistDao.delete(id)
}
