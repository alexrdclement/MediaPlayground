package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.ImageFile
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageFileDao {
    @Query("SELECT * FROM image_files WHERE id = :id")
    suspend fun getImage(id: String): ImageFile?

    @Query("SELECT * FROM image_files WHERE id = :id")
    fun getImageFlow(id: String): Flow<ImageFile?>

    @Query("SELECT * FROM image_files ORDER BY id")
    fun getImagesPagingSource(): PagingSource<Int, ImageFile>

    @Query("SELECT COUNT(*) FROM image_files")
    fun getImageCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg image: ImageFile)

    @Update
    suspend fun update(image: ImageFile)

    @Query("DELETE FROM image_files WHERE id = :id")
    suspend fun delete(id: String)
}
