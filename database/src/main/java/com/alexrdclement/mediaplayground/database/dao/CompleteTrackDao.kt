package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface CompleteTrackDao {
    @Transaction
    @Query("SELECT * FROM CompleteTrack ORDER BY modifiedDate DESC")
    suspend fun getTracks(): List<CompleteTrack>

    @Transaction
    @Query("SELECT * FROM CompleteTrack ORDER BY modifiedDate DESC")
    fun getTracksFlow(): Flow<List<CompleteTrack>>

    @Transaction
    @Query("SELECT * FROM CompleteTrack WHERE id = :id")
    suspend fun getTrack(id: String): CompleteTrack?
}
