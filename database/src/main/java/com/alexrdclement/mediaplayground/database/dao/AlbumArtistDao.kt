package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumWithArtists

@Dao
interface AlbumArtistDao {
    @Transaction
    @Query(
        """
        SELECT * FROM albums
        JOIN albumArtists ON albums.id = albumArtists.albumId
        WHERE albums.title = :title AND albumArtists.artistId = :artistId
        """,
    )
    suspend fun getAlbumByTitleAndArtistId(title: String, artistId: String): AlbumWithArtists?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg albumArist: AlbumArtistCrossRef)
}
