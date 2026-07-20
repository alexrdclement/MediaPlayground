package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.ImageAsset
import com.alexrdclement.mediaplayground.database.model.MediaItemImageCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaItemImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg crossRef: MediaItemImageCrossRef)

    @Delete
    suspend fun delete(crossRef: MediaItemImageCrossRef)

    @Query("DELETE FROM media_item_images WHERE item_id = :itemId")
    suspend fun deleteForItem(itemId: String)

    @Query("""
        SELECT image_assets.* FROM image_assets
        INNER JOIN media_item_images ON image_assets.id = media_item_images.image_asset_id
        WHERE media_item_images.item_id = :itemId
    """)
    fun getImagesForItemFlow(itemId: String): Flow<List<ImageAsset>>
}
