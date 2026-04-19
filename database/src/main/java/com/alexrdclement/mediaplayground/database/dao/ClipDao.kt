package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.Clip
import kotlinx.coroutines.flow.Flow

@Dao
interface ClipDao {
    @Query("SELECT * FROM clips WHERE id = :id")
    suspend fun getClip(id: String): Clip?

    @Query("SELECT * FROM clips WHERE id = :id")
    fun getClipFlow(id: String): Flow<Clip?>

    @Query("SELECT COUNT(*) FROM clips")
    fun getClipCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg clip: Clip)

    @Update
    suspend fun update(clip: Clip)

    @Query("DELETE FROM clips WHERE id = :id")
    suspend fun delete(id: String)
}
