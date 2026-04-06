package com.alexrdclement.mediaplayground.data.clip.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.database.dao.CompleteAudioClipDao
import com.alexrdclement.mediaplayground.database.mapping.toAudioFileEntity
import com.alexrdclement.mediaplayground.database.mapping.toClip
import com.alexrdclement.mediaplayground.database.mapping.toClipEntity
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteClip
import com.alexrdclement.mediaplayground.database.transaction.insertClip
import com.alexrdclement.mediaplayground.database.transaction.updateClip
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalClipDataStore @Inject constructor(
    private val completeAudioClipDao: CompleteAudioClipDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) {
    suspend fun get(id: ClipId): Clip? {
        return completeAudioClipDao.getClip(id.value)?.toClip()
    }

    fun getClipFlow(id: ClipId): Flow<Clip?> {
        return completeAudioClipDao.getClipFlow(id.value).map { it?.toClip() }
    }

    fun getClipPagingData(config: PagingConfig): Flow<PagingData<Clip>> {
        return Pager(config = config) {
            completeAudioClipDao.getClipsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toClip() }
        }
    }

    fun getClipCountFlow(): Flow<Int> = completeAudioClipDao.getClipCountFlow()

    suspend fun put(clip: Clip) = databaseTransactionRunner.run {
        insertClip(
            clip = clip.toClipEntity(),
            audioFile = clip.mediaAsset.toAudioFileEntity(),
        )
    }

    suspend fun updateClipTitle(id: ClipId, title: String) = databaseTransactionRunner.run {
        updateClip(
            id = id.value,
            title = title,
        )
    }

    suspend fun delete(id: ClipId) = databaseTransactionRunner.run {
        deleteClip(id = id.value)
    }
}
