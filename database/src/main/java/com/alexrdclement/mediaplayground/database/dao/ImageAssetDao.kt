package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset
import com.alexrdclement.mediaplayground.database.model.ImageAsset
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageAssetDao {
    @Transaction
    @Query("SELECT * FROM image_assets WHERE id = :id")
    suspend fun getImage(id: String): CompleteImageAsset?

    @Transaction
    @Query("SELECT * FROM image_assets WHERE id = :id")
    fun getImageFlow(id: String): Flow<CompleteImageAsset?>

    @Transaction
    @Query("SELECT * FROM image_assets ORDER BY id")
    fun getImagesPagingSource(): PagingSource<Int, CompleteImageAsset>

    @Query("SELECT COUNT(*) FROM image_assets")
    fun getImageCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg image: ImageAsset)

    @Update
    suspend fun update(image: ImageAsset)

    @Query("DELETE FROM image_assets WHERE id = :id")
    suspend fun delete(id: String)
}
