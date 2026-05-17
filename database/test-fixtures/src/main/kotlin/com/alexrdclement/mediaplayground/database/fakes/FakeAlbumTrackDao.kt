package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.AlbumTrackDao
import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAlbumTrackDao : AlbumTrackDao {

    val albumTrackRefs = MutableStateFlow<List<AlbumTrackCrossRef>>(emptyList())

    override suspend fun insert(crossRef: AlbumTrackCrossRef) {
        if (albumTrackRefs.value.none { it.albumId == crossRef.albumId && it.trackId == crossRef.trackId }) {
            albumTrackRefs.value = albumTrackRefs.value + crossRef
        }
    }

    override suspend fun getTrackIdsForAlbum(albumId: String): List<String> {
        return albumTrackRefs.value.filter { it.albumId == albumId }.map { it.trackId }
    }

    override suspend fun getAlbumIdsForTrack(trackId: String): List<String> {
        return albumTrackRefs.value.filter { it.trackId == trackId }.map { it.albumId }
    }

    override suspend fun updateTrackNumber(trackId: String, trackNumber: Int?) {
        albumTrackRefs.value = albumTrackRefs.value.map { ref ->
            if (ref.trackId == trackId) ref.copy(trackNumber = trackNumber) else ref
        }
    }

    override suspend fun deleteForAlbum(albumId: String) {
        albumTrackRefs.value = albumTrackRefs.value.filterNot { it.albumId == albumId }
    }
}
