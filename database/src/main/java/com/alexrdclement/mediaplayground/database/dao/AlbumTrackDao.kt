package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef

@Dao
interface AlbumTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(crossRef: AlbumTrackCrossRef)

    @Query("SELECT track_id FROM album_tracks WHERE album_id = :albumId")
    suspend fun getTrackIdsForAlbum(albumId: String): List<String>

    @Query("SELECT album_id FROM album_tracks WHERE track_id = :trackId")
    suspend fun getAlbumIdsForTrack(trackId: String): List<String>

    @Query("UPDATE album_tracks SET track_number = :trackNumber WHERE track_id = :trackId")
    suspend fun updateTrackNumber(trackId: String, trackNumber: Int?)

    @Query("DELETE FROM album_tracks WHERE album_id = :albumId")
    suspend fun deleteForAlbum(albumId: String)
}
