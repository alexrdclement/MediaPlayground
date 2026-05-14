package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import kotlinx.coroutines.flow.Flow

@Dao
interface CompleteAlbumDao {
    @Query("SELECT COUNT(*) FROM albums")
    fun getAlbumCountFlow(): Flow<Int>

    @Transaction
    @Query("SELECT albums.* FROM albums JOIN media_collections ON albums.id = media_collections.id ORDER BY media_collections.modified_at DESC")
    fun getAlbumsPagingSource(): PagingSource<Int, CompleteAlbum>

    @Transaction
    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbum(id: String): CompleteAlbum?

    @Transaction
    @Query("SELECT * FROM albums WHERE id = :id")
    fun getAlbumFlow(id: String): Flow<CompleteAlbum?>
}
