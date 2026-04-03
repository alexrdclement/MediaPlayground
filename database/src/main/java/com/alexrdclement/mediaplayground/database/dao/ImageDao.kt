package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE id = :id")
    suspend fun getImage(id: String): Image?

    @Query("SELECT * FROM images WHERE id = :id")
    fun getImageFlow(id: String): Flow<Image?>

    @Query("SELECT * FROM images ORDER BY id")
    fun getImagesPagingSource(): PagingSource<Int, Image>

    @Query("SELECT COUNT(*) FROM images")
    fun getImageCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg image: Image)

    @Update
    suspend fun update(image: Image)

    @Query("DELETE FROM images WHERE id = :id")
    suspend fun delete(id: String)
}
