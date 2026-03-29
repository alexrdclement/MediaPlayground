package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.Image
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg albumImage: AlbumImageCrossRef)

    @Delete
    suspend fun delete(albumImage: AlbumImageCrossRef)

    @Query("DELETE FROM album_images WHERE album_id = :albumId")
    suspend fun deleteForAlbum(albumId: String)

    @Query("""
        SELECT images.* FROM images
        INNER JOIN album_images ON images.id = album_images.image_id
        WHERE album_images.album_id = :albumId
    """)
    fun getImagesForAlbumFlow(albumId: String): Flow<List<Image>>
}
