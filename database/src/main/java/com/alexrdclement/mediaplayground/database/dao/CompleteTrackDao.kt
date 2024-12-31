package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface CompleteTrackDao {
    @Transaction
    @Query("SELECT * FROM tracks ORDER BY modifiedDate DESC")
    suspend fun getTracks(): List<CompleteTrack>

    @Transaction
    @Query("SELECT * FROM tracks ORDER BY modifiedDate DESC")
    fun getTracksFlow(): Flow<List<CompleteTrack>>

    @Transaction
    @Query("SELECT * FROM tracks ORDER BY modifiedDate DESC")
    fun getTracksPagingSource(): PagingSource<Int, CompleteTrack>

    @Transaction
    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrack(id: String): CompleteTrack?

    @Query("DELETE FROM tracks WHERE id = :id")
    suspend fun delete(id: String)
}
