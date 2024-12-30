package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum

@Dao
interface SimpleAlbumDao {
    @Transaction
    @Query(
        """
        SELECT * FROM albums
        JOIN albumArtists ON albums.id = albumArtists.albumId
        WHERE albums.title = :title AND albumArtists.artistId = :artistId
        """,
    )
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAlbumByTitleAndArtistId(title: String, artistId: String): SimpleAlbum?
}
