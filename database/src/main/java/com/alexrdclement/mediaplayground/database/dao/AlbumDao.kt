package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.Album

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbum(id: String): Album?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(album: Album)

    @Update
    suspend fun update(album: Album)

    @Query("DELETE FROM albums WHERE id = :id")
    suspend fun delete(id: String)
}
