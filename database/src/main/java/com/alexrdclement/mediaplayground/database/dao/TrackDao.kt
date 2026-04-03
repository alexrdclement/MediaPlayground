package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.Track
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Query("SELECT COUNT(*) FROM tracks")
    fun getTrackCountFlow(): Flow<Int>

    @Query("SELECT * FROM tracks WHERE album_id = :albumId ORDER BY track_number")
    suspend fun getTracksForAlbum(albumId: String): List<Track>

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrack(id: String): Track?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg track: Track)

    @Update
    suspend fun update(track: Track)

    @Query("DELETE FROM tracks WHERE id = :id")
    suspend fun delete(id: String)
}
