package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip
import kotlinx.coroutines.flow.Flow

@Dao
interface CompleteAudioClipDao {
    @Transaction
    @Query("SELECT * FROM clips WHERE id = :id")
    suspend fun getClip(id: String): CompleteAudioClip?

    @Transaction
    @Query("SELECT * FROM clips WHERE id = :id")
    fun getClipFlow(id: String): Flow<CompleteAudioClip?>

    @Transaction
    @Query("SELECT * FROM clips ORDER BY id")
    fun getClipsPagingSource(): PagingSource<Int, CompleteAudioClip>

    @Transaction
    @Query("SELECT COUNT(*) FROM clips")
    fun getClipCountFlow(): Flow<Int>
}
