package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.AudioAssetImageCrossRef

@Dao
interface AudioAssetImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg crossRef: AudioAssetImageCrossRef)

    @Query("DELETE FROM audio_asset_images WHERE audio_asset_id = :audioAssetId")
    suspend fun deleteForAudioAsset(audioAssetId: String)
}
