package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum

@Dao
interface CompleteAlbumDao {
    @Transaction
    @Query("SELECT * FROM albums ORDER BY modified_date DESC")
    fun getAlbumsPagingSource(): PagingSource<Int, CompleteAlbum>

    @Transaction
    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getAlbum(id: String): CompleteAlbum?
}
