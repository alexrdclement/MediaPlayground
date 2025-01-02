package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum

@Dao
interface SimpleAlbumDao {
    @Transaction
    @Query(
        """
        SELECT * FROM albums
        JOIN album_artists ON albums.id = album_artists.album_id
        WHERE albums.title = :title AND album_artists.artist_id = :artistId
        """,
    )
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAlbumByTitleAndArtistId(title: String, artistId: String): SimpleAlbum?
}
