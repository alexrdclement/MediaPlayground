package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import kotlinx.coroutines.flow.Flow

@Dao
interface CompleteTrackDao {
    @Query("SELECT COUNT(*) FROM tracks")
    fun getTrackCountFlow(): Flow<Int>

    @Transaction
    @Query("SELECT tracks.* FROM tracks JOIN media_items ON tracks.id = media_items.id ORDER BY media_items.modified_at DESC")
    fun getTracksPagingSource(): PagingSource<Int, CompleteTrack>

    @Transaction
    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrack(id: String): CompleteTrack?

    @Transaction
    @Query("SELECT * FROM tracks WHERE id = :id")
    fun getTrackFlow(id: String): Flow<CompleteTrack?>

    @Transaction
    @Query("SELECT tracks.* FROM tracks INNER JOIN track_clips ON tracks.id = track_clips.track_id WHERE track_clips.clip_id = :clipId LIMIT 1")
    suspend fun getTrackByClipId(clipId: String): CompleteTrack?
}
