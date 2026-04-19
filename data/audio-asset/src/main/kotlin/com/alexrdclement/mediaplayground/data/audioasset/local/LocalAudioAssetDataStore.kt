package com.alexrdclement.mediaplayground.data.audioasset.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.database.dao.AudioAssetDao
import com.alexrdclement.mediaplayground.database.mapping.toAudioAsset
import com.alexrdclement.mediaplayground.database.mapping.toAudioAssetEntity
import com.alexrdclement.mediaplayground.database.mapping.toMediaAssetRecord
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteAudioAsset
import com.alexrdclement.mediaplayground.database.transaction.insertAudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalAudioAssetDataStore @Inject constructor(
    private val audioAssetDao: AudioAssetDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) {
    suspend fun getAudioAsset(id: AudioAssetId): AudioAsset? =
        audioAssetDao.getAudioAsset(id.value)?.toAudioAsset()

    suspend fun getByFileName(fileName: String): AudioAsset? =
        audioAssetDao.getAudioAssetByFileName(fileName)?.toAudioAsset()

    fun getAudioAssetFlow(id: AudioAssetId): Flow<AudioAsset?> =
        audioAssetDao.getAudioAssetFlow(id.value).map { it?.toAudioAsset() }

    fun getAudioAssetPagingData(config: PagingConfig): Flow<PagingData<AudioAsset>> =
        Pager(config = config) {
            audioAssetDao.getAudioAssetsPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toAudioAsset() }
        }

    fun getAudioAssetCountFlow(): Flow<Int> = audioAssetDao.getAudioAssetCountFlow()

    suspend fun put(audioAsset: AudioAsset) = databaseTransactionRunner.run {
        insertAudioAsset(
            mediaAsset = audioAsset.toMediaAssetRecord(),
            audioAsset = audioAsset.toAudioAssetEntity(),
        )
    }

    suspend fun delete(id: AudioAssetId) = databaseTransactionRunner.run {
        deleteAudioAsset(id.value)
    }
}
