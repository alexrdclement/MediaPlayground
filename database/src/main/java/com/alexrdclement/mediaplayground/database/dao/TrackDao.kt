package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks WHERE albumId = :albumId ORDER BY trackNumber")
    suspend fun getTracks(albumId: String): List<Track>

    @Query("SELECT * FROM tracks WHERE albumId = :albumId ORDER BY trackNumber")
    fun getTracksFlow(albumId: String): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrack(id: String): Track?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: Track)
}
