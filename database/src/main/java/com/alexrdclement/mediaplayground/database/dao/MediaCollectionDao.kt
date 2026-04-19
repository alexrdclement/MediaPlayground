package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.MediaCollection

@Dao
interface MediaCollectionDao {
    @Query("SELECT * FROM media_collections WHERE id = :id")
    suspend fun getMediaCollection(id: String): MediaCollection?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg mediaCollection: MediaCollection)

    @Update
    suspend fun update(mediaCollection: MediaCollection)

    @Query("DELETE FROM media_collections WHERE id = :id")
    suspend fun delete(id: String)
}
