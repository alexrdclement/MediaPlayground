package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.model.Album
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAlbumDao : AlbumDao {

    val albums = MutableStateFlow(emptySet<Album>())

    override suspend fun getAlbum(id: String): Album? {
        return albums.value.find { it.id == id }
    }

    override suspend fun insert(album: Album) {
        if (albums.value.any { it.id == album.id }) return
        albums.value = albums.value + album
    }

    override suspend fun update(album: Album) {
        val existing = albums.value.find { it.id == album.id } ?: return
        albums.value = albums.value - existing + album
    }

    override suspend fun delete(id: String) {
        val existingAlbum = albums.value.find { it.id == id } ?: return
        albums.value -= existingAlbum
    }
}
