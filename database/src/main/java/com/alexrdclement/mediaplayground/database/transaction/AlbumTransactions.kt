package com.alexrdclement.mediaplayground.database.transaction

import com.alexrdclement.mediaplayground.database.model.Album
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.MediaCollection
import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.database.model.MediaItemType
import com.alexrdclement.mediaplayground.media.model.deletion.DeleteAlbumPolicy

context(scope: DatabaseTransactionScope)
suspend fun insertAlbum(
    mediaCollection: MediaCollection,
    album: Album,
    artistIds: Set<String>,
    imageIds: Set<String>,
) = with(scope) {
    mediaItemDao.insert(MediaItem(id = mediaCollection.id, itemType = MediaItemType.COLLECTION))
    mediaCollectionDao.insert(mediaCollection)
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
suspend fun deleteAlbum(
    id: String,
    policy: DeleteAlbumPolicy = DeleteAlbumPolicy(),
) = with(scope) {
    val orphanedTrackIds = if (policy.deleteOrphanedTracks) {
        val trackIds = albumTrackDao.getTrackIdsForAlbum(id)
        trackIds.filter { trackId ->
            albumTrackDao.getAlbumIdsForTrack(trackId) == listOf(id)
        }
    } else {
        emptyList()
    }
    albumImageDao.deleteForAlbum(id)
    albumArtistDao.deleteForAlbum(id)
    albumTrackDao.deleteForAlbum(id)
    albumDao.delete(id)
    mediaCollectionDao.delete(id)
    mediaItemDao.delete(id)
    for (trackId in orphanedTrackIds) {
        deleteTrack(trackId, policy.trackPolicy)
    }
}
