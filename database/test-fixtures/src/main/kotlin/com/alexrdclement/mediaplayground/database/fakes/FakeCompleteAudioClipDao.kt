package com.alexrdclement.mediaplayground.database.fakes

import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.alexrdclement.mediaplayground.database.dao.CompleteAudioClipDao
import com.alexrdclement.mediaplayground.database.model.CompleteAudioClip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class FakeCompleteAudioClipDao(
    val coroutineScope: CoroutineScope,
    val clipDao: FakeClipDao,
    val audioFileDao: FakeAudioFileDao,
) : CompleteAudioClipDao {

    val completeClips = combine(
        clipDao.clips,
        audioFileDao.audioFiles,
    ) { clips, audioFiles ->
        clips.mapNotNull { clip ->
            val audioFile = audioFiles.find { it.id == clip.assetId } ?: return@mapNotNull null
            CompleteAudioClip(clip = clip, audioFile = audioFile)
        }
    }

    override suspend fun getClip(id: String): CompleteAudioClip? {
        val clips = clipDao.clips.value
        val audioFiles = audioFileDao.audioFiles.value
        val clip = clips.find { it.id == id } ?: return null
        val audioFile = audioFiles.find { it.id == clip.assetId } ?: return null
        return CompleteAudioClip(clip = clip, audioFile = audioFile)
    }

    override fun getClipFlow(id: String): Flow<CompleteAudioClip?> {
        return completeClips.map { it.find { completeClip -> completeClip.clip.id == id } }
    }

    override fun getClipsPagingSource(): PagingSource<Int, CompleteAudioClip> {
        return completeClips.asPagingSourceFactory(coroutineScope).invoke()
    }

    override fun getClipCountFlow(): Flow<Int> {
        return completeClips.map { it.size }
    }
}
