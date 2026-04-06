package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef

@Dao
interface TrackClipDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg crossRef: TrackClipCrossRef)

    @Query("SELECT clip_id FROM track_clips WHERE track_id = :trackId")
    suspend fun getClipIdsForTrack(trackId: String): List<String>
}
