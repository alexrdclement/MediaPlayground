package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.Track

@Dao
interface TrackDao {
    @Query("SELECT tracks.* FROM tracks INNER JOIN album_tracks ON tracks.id = album_tracks.track_id WHERE album_tracks.album_id = :albumId ORDER BY album_tracks.track_number")
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
