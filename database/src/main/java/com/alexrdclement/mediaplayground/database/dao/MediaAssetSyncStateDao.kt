package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.MediaAssetSyncStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaAssetSyncStateDao {
    @Query("SELECT * FROM media_asset_sync_states WHERE media_asset_id = :id")
    fun getFlow(id: String): Flow<MediaAssetSyncStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: MediaAssetSyncStateEntity)

    @Query("DELETE FROM media_asset_sync_states WHERE media_asset_id = :id")
    suspend fun delete(id: String)
}
