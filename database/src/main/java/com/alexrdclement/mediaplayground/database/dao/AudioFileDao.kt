package com.alexrdclement.mediaplayground.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alexrdclement.mediaplayground.database.model.AudioFile
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioFileDao {
    @Query("SELECT * FROM audio_files WHERE id = :id")
    suspend fun getAudioFile(id: String): AudioFile?

    @Query("SELECT * FROM audio_files WHERE file_name = :fileName LIMIT 1")
    suspend fun getAudioFileByFileName(fileName: String): AudioFile?

    @Query("SELECT * FROM audio_files WHERE id = :id")
    fun getAudioFileFlow(id: String): Flow<AudioFile?>

    @Query("SELECT * FROM audio_files ORDER BY id")
    fun getAudioFilesPagingSource(): PagingSource<Int, AudioFile>

    @Query("SELECT COUNT(*) FROM audio_files")
    fun getAudioFileCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg audioFile: AudioFile)

    @Update
    suspend fun update(audioFile: AudioFile)

    @Query("DELETE FROM audio_files WHERE id = :id")
    suspend fun delete(id: String)
}
