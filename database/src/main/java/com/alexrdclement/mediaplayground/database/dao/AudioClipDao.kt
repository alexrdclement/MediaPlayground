package com.alexrdclement.mediaplayground.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.alexrdclement.mediaplayground.database.model.AudioClip

@Dao
interface AudioClipDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg audioClip: AudioClip)
}
