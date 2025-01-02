package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.CompleteTrack

@Dao
interface CompleteTrackDao {
    @Transaction
    @Query("SELECT * FROM tracks ORDER BY modified_date DESC")
    fun getTracksPagingSource(): PagingSource<Int, CompleteTrack>

    @Transaction
    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrack(id: String): CompleteTrack?
}
