package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.MediaAsset

@Dao
interface MediaAssetDao {
    @Query("SELECT * FROM media_assets WHERE id = :id")
    suspend fun getMediaAsset(id: String): MediaAsset?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg mediaAsset: MediaAsset)

    @Query("DELETE FROM media_assets WHERE id = :id")
    suspend fun delete(id: String)
}
