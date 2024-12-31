package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.Album
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT COUNT(*) FROM albums")
    fun getAlbumCountFlow(): Flow<Int>

    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbum(id: String): Album?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: Album)

    @Query("DELETE FROM albums WHERE id = :id")
    suspend fun delete(id: String)
}
