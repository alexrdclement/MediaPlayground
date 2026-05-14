package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.AudioAssetArtistCrossRef

@Dao
interface AudioAssetArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg crossRef: AudioAssetArtistCrossRef)

    @Query("DELETE FROM audio_asset_artists WHERE audio_asset_id = :audioAssetId")
    suspend fun deleteForAudioAsset(audioAssetId: String)
}
