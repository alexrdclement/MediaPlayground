package com.alexrdclement.mediaplayground.data.clip.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.database.dao.CompleteAudioClipDao
import com.alexrdclement.mediaplayground.database.mapping.toAudioAssetEntity
import com.alexrdclement.mediaplayground.database.mapping.toAudioClip
import com.alexrdclement.mediaplayground.database.mapping.toAudioClipEntity
import com.alexrdclement.mediaplayground.database.mapping.toClipEntity
import com.alexrdclement.mediaplayground.database.mapping.toMediaAssetRecord
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteClip
import com.alexrdclement.mediaplayground.database.transaction.insertClip
import com.alexrdclement.mediaplayground.database.transaction.updateClip
import com.alexrdclement.mediaplayground.media.model.AudioClip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.model.deletion.DeleteClipPolicy
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalClipDataStore @Inject constructor(
    private val completeAudioClipDao: CompleteAudioClipDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) {
    suspend fun get(id: ClipId): AudioClip? {
        return completeAudioClipDao.getClip(id.value)?.toAudioClip()
    }

    suspend fun getByMediaAssetId(id: MediaAssetId): AudioClip? {
        return completeAudioClipDao.getClipByMediaAssetId(id.value)?.toAudioClip()
    }

    fun getClipFlow(id: ClipId): Flow<AudioClip?> {
        return completeAudioClipDao.getClipFlow(id.value).map { it?.toAudioClip() }
    }

    fun getClipPagingData(config: PagingConfig): Flow<PagingData<AudioClip>> {
        return Pager(config = config) {
            completeAudioClipDao.getClipsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toAudioClip() }
        }
    }

    fun getClipCountFlow(): Flow<Int> = completeAudioClipDao.getClipCountFlow()

    suspend fun put(clip: AudioClip) = databaseTransactionRunner.run {
        insertClip(
            clip = clip.toClipEntity(),
            audioClip = clip.toAudioClipEntity(),
            mediaAsset = clip.mediaAsset.toMediaAssetRecord(),
            audioAsset = clip.mediaAsset.toAudioAssetEntity(),
        )
    }

    suspend fun updateClipTitle(id: ClipId, title: String) = databaseTransactionRunner.run {
        updateClip(
            id = id.value,
            title = title,
        )
    }

    suspend fun delete(id: ClipId, policy: DeleteClipPolicy = DeleteClipPolicy()) = databaseTransactionRunner.run {
        deleteClip(id = id.value, policy = policy)
    }
}
