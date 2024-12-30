package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef

@Dao
interface AlbumArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg albumArist: AlbumArtistCrossRef)
}
