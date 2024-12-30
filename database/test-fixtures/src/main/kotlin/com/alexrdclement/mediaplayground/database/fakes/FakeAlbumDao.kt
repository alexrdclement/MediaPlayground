package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.model.Album
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeAlbumDao : AlbumDao {

    val albums = MutableStateFlow(emptySet<Album>())

    override suspend fun getAlbums(): List<Album> {
        return albums.value.toList()
    }

    override fun getAlbumsFlow(): Flow<List<Album>> {
        return albums.map { it.toList() }
    }

    override suspend fun getAlbum(id: String): Album? {
        return albums.value.find { it.id == id }
    }

    override suspend fun insert(album: Album) {
        val existingAlbum = albums.value.find { it.id == album.id }
        val newAlbums = albums.value.toMutableSet()
        if (existingAlbum != null) {
            newAlbums -= existingAlbum
        }
        albums.value = newAlbums + album
    }

    override suspend fun delete(id: String) {
        val existingAlbum = albums.value.find { it.id == id } ?: return
        albums.value -= existingAlbum
    }
}
