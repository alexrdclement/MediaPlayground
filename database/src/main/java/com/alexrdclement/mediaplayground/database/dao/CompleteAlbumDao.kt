package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import kotlinx.coroutines.flow.Flow

@Dao
interface CompleteAlbumDao {
    @Transaction
    @Query("SELECT * FROM albums ORDER BY modifiedDate DESC")
    suspend fun getAlbums(): List<CompleteAlbum>

    @Transaction
    @Query("SELECT * FROM albums ORDER BY modifiedDate DESC")
    fun getAlbumsFlow(): Flow<List<CompleteAlbum>>

    @Transaction
    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbum(id: String): CompleteAlbum?
}
