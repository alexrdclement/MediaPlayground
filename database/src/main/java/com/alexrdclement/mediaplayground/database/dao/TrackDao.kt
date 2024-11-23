package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM track ORDER BY modifiedDate DESC")
    suspend fun getTracks(): List<Track>

    @Query("SELECT * FROM track ORDER BY modifiedDate DESC")
    fun getTracksFlow(): Flow<List<Track>>

    @Query("SELECT * FROM track WHERE id = :id")
    suspend fun getTrack(id: String): Track?

    @Insert
    suspend fun insert(track: Track)
}
