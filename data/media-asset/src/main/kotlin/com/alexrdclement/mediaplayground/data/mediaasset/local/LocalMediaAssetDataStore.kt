package com.alexrdclement.mediaplayground.data.mediaasset.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.database.dao.AudioFileDao
import com.alexrdclement.mediaplayground.database.mapping.toAudioFileEntity
import com.alexrdclement.mediaplayground.database.mapping.toMediaAsset
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteAudioFile
import com.alexrdclement.mediaplayground.database.transaction.insertAudioFile
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMediaAssetDataStore @Inject constructor(
    private val audioFileDao: AudioFileDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) {
    suspend fun getMediaAsset(id: MediaAssetId): MediaAsset? {
        return audioFileDao.getAudioFile(id.value)?.toMediaAsset()
    }

    fun getMediaAssetFlow(id: MediaAssetId): Flow<MediaAsset?> {
        return audioFileDao.getAudioFileFlow(id.value).map { it?.toMediaAsset() }
    }

    fun getMediaAssetPagingData(config: PagingConfig): Flow<PagingData<MediaAsset>> {
        return Pager(config = config) {
            audioFileDao.getAudioFilesPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toMediaAsset() }
        }
    }

    fun getMediaAssetCountFlow(): Flow<Int> = audioFileDao.getAudioFileCountFlow()

    suspend fun put(mediaAsset: MediaAsset) {
        databaseTransactionRunner.run {
            insertAudioFile(mediaAsset.toAudioFileEntity())
        }
    }

    suspend fun delete(id: MediaAssetId) = databaseTransactionRunner.run {
        deleteAudioFile(id.value)
    }
}
