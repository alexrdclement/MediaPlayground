package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums ORDER BY modifiedDate DESC")
    suspend fun getAlbums(): List<Album>

    @Query("SELECT * FROM albums ORDER BY modifiedDate DESC")
    fun getAlbumsFlow(): Flow<List<Album>>

    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbum(id: String): Album?

    @Query("SELECT * FROM albums WHERE title = :title AND artistId = :artistId")
    suspend fun getAlbumByTitleAndArtistId(title: String, artistId: String): Album?

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(album: Album)
}
