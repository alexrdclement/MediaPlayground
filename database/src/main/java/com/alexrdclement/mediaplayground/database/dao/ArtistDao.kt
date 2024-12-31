package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexrdclement.mediaplayground.database.model.Artist

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists WHERE id = :id")
    suspend fun getArtist(id: String): Artist?

    @Query("SELECT * FROM artists WHERE name = :name")
    suspend fun getArtistByName(name: String): Artist?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(artist: Artist)

    @Query("DELETE FROM artists WHERE id = :id")
    suspend fun delete(id: String)
}
