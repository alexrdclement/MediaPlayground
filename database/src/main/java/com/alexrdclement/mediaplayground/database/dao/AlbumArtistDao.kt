package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef

@Dao
interface AlbumArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg albumArist: AlbumArtistCrossRef)

    @Query("SELECT * FROM album_artists WHERE album_id = :albumId")
    suspend fun getAlbumArtists(albumId: String): List<AlbumArtistCrossRef>

    @Query("SELECT * FROM album_artists WHERE artist_id = :artistId")
    suspend fun getArtistAlbums(artistId: String): List<AlbumArtistCrossRef>

    @Delete
    suspend fun delete(albumArtist: AlbumArtistCrossRef)
}
