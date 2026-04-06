package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.Album
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef

context(scope: DatabaseTransactionScope)
suspend fun insertAlbum(
    album: Album,
    artistIds: Set<String>,
    imageIds: Set<String>,
) = with(scope) {
    albumDao.insert(album)
    val albumArtistCrossRefs = artistIds.map { AlbumArtistCrossRef(album.id, it) }
    albumArtistDao.insert(*albumArtistCrossRefs.toTypedArray())
    val albumImageCrossRefs = imageIds.map { AlbumImageCrossRef(album.id, it) }
    albumImageDao.insert(*albumImageCrossRefs.toTypedArray())
}

context(scope: DatabaseTransactionScope)
suspend fun updateAlbum(
    album: Album,
) = with(scope) {
    albumDao.update(album)
}

context(scope: DatabaseTransactionScope)
suspend fun deleteAlbum(id: String) = with(scope) {
    albumImageDao.deleteForAlbum(id)
    albumArtistDao.deleteForAlbum(id)
    albumDao.delete(id)
}
