package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.MediaItem

@Dao
interface MediaItemDao {
    @Query("SELECT * FROM media_items WHERE id = :id")
    suspend fun getMediaItem(id: String): MediaItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg mediaItem: MediaItem)

    @Update
    suspend fun update(vararg mediaItem: MediaItem)

    @Query("DELETE FROM media_items WHERE id = :id")
    suspend fun delete(id: String)
}
