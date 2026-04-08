package com.alexrdclement.mediaplayground.database.fakes

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.database.dao.AudioFileDao
import com.alexrdclement.mediaplayground.database.model.AudioFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeAudioFileDao : AudioFileDao {

    val audioFiles = MutableStateFlow(emptySet<AudioFile>())

    override suspend fun getAudioFile(id: String): AudioFile? {
        return audioFiles.value.find { it.id == id }
    }

    override suspend fun getAudioFileByFileName(fileName: String): AudioFile? {
        return audioFiles.value.find { it.fileName == fileName }
    }

    override fun getAudioFileFlow(id: String): Flow<AudioFile?> {
        return audioFiles.map { it.find { audioFile -> audioFile.id == id } }
    }

    override fun getAudioFilesPagingSource(): PagingSource<Int, AudioFile> {
        return object : PagingSource<Int, AudioFile>() {
            override fun getRefreshKey(state: PagingState<Int, AudioFile>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AudioFile> {
                return LoadResult.Page(
                    data = audioFiles.value.toList(),
                    prevKey = null,
                    nextKey = null,
                )
            }
        }
    }

    override fun getAudioFileCountFlow(): Flow<Int> = audioFiles.map { it.size }

    override suspend fun insert(vararg audioFile: AudioFile) {
        for (newAudioFile in audioFile) {
            if (audioFiles.value.any { it.id == newAudioFile.id }) continue
            audioFiles.value += newAudioFile
        }
    }

    override suspend fun update(audioFile: AudioFile) {
        val existing = audioFiles.value.find { it.id == audioFile.id } ?: return
        audioFiles.value = audioFiles.value - existing + audioFile
    }

    override suspend fun delete(id: String) {
        val existing = audioFiles.value.find { it.id == id } ?: return
        audioFiles.value -= existing
    }
}
