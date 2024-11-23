package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.Track

@Dao
interface TrackDao {
    @Query("SELECT * FROM track")
    fun getTracks(): List<Track>

    @Insert
    fun insert(track: Track)
}
