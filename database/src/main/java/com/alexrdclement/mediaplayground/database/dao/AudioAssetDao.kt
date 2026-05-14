package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioAssetDao {
    @Transaction
    @Query("SELECT * FROM audio_assets WHERE id = :id")
    suspend fun getAudioAsset(id: String): CompleteAudioAsset?

    @Transaction
    @Query("SELECT audio_assets.* FROM audio_assets INNER JOIN media_assets ON audio_assets.id = media_assets.id WHERE media_assets.file_name = :fileName LIMIT 1")
    suspend fun getAudioAssetByFileName(fileName: String): CompleteAudioAsset?

    @Transaction
    @Query("SELECT * FROM audio_assets WHERE id = :id")
    fun getAudioAssetFlow(id: String): Flow<CompleteAudioAsset?>

    @Transaction
    @Query("SELECT * FROM audio_assets ORDER BY id")
    fun getAudioAssetsPagingSource(): PagingSource<Int, CompleteAudioAsset>

    @Query("SELECT COUNT(*) FROM audio_assets")
    fun getAudioAssetCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg audioAsset: AudioAsset)

    @Update
    suspend fun update(audioAsset: AudioAsset)

    @Query("DELETE FROM audio_assets WHERE id = :id")
    suspend fun delete(id: String)
}
