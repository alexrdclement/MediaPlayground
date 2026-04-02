package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.Artist
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Query("SELECT * FROM artists WHERE id = :id")
    suspend fun getArtist(id: String): Artist?

    @Query("SELECT * FROM artists WHERE id = :id")
    fun getArtistFlow(id: String): Flow<Artist?>

    @Query("SELECT * FROM artists WHERE name = :name")
    suspend fun getArtistByName(name: String): Artist?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg artist: Artist)

    @Update
    suspend fun update(artist: Artist)

    @Query("DELETE FROM artists WHERE id = :id")
    suspend fun delete(id: String)
}
