package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.Image

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE id = :id")
    suspend fun getImage(id: String): Image?

    @Query("SELECT * FROM images WHERE albumId = :albumId")
    suspend fun getImagesForAlbum(albumId: String): List<Image>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg image: Image)

    @Query("DELETE FROM images WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM images WHERE albumId = :albumId")
    suspend fun deleteImagesForAlbum(albumId: String)
}
