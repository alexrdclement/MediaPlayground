package com.alexrdclement.mediaplayground.database.fakes

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.database.dao.AudioAssetDao
import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class FakeAudioAssetDao(
    val mediaAssetDao: FakeMediaAssetDao = FakeMediaAssetDao(),
) : AudioAssetDao {

    val audioAssets = MutableStateFlow(emptySet<AudioAsset>())

    private fun buildCompleteAudioAsset(audioAsset: AudioAsset): CompleteAudioAsset? {
        val mediaAsset = mediaAssetDao.mediaAssets[audioAsset.id] ?: return null
        return CompleteAudioAsset(audioAsset = audioAsset, mediaAsset = mediaAsset)
    }

    override suspend fun getAudioAsset(id: String): CompleteAudioAsset? {
        val audioAsset = audioAssets.value.find { it.id == id } ?: return null
        return buildCompleteAudioAsset(audioAsset)
    }

    override suspend fun getAudioAssetByFileName(fileName: String): CompleteAudioAsset? {
        val mediaAsset = mediaAssetDao.mediaAssets.values.find { it.fileName == fileName } ?: return null
        val audioAsset = audioAssets.value.find { it.id == mediaAsset.id } ?: return null
        return CompleteAudioAsset(audioAsset = audioAsset, mediaAsset = mediaAsset)
    }

    override fun getAudioAssetFlow(id: String): Flow<CompleteAudioAsset?> {
        return combine(audioAssets, mediaAssetDao.mediaAssetsFlow) { files, _ ->
            val audioAsset = files.find { it.id == id } ?: return@combine null
            buildCompleteAudioAsset(audioAsset)
        }
    }

    override fun getAudioAssetsPagingSource(): PagingSource<Int, CompleteAudioAsset> {
        return object : PagingSource<Int, CompleteAudioAsset>() {
            override fun getRefreshKey(state: PagingState<Int, CompleteAudioAsset>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CompleteAudioAsset> {
                val completeFiles = audioAssets.value.mapNotNull { buildCompleteAudioAsset(it) }
                return LoadResult.Page(
                    data = completeFiles,
                    prevKey = null,
                    nextKey = null,
                )
            }
        }
    }

    override fun getAudioAssetCountFlow(): Flow<Int> = audioAssets.map { it.size }

    override suspend fun insert(vararg audioAsset: AudioAsset) {
        for (newAudioAsset in audioAsset) {
            if (audioAssets.value.any { it.id == newAudioAsset.id }) continue
            audioAssets.value += newAudioAsset
        }
    }

    override suspend fun update(audioAsset: AudioAsset) {
        val existing = audioAssets.value.find { it.id == audioAsset.id } ?: return
        audioAssets.value = audioAssets.value - existing + audioAsset
    }

    override suspend fun delete(id: String) {
        val existing = audioAssets.value.find { it.id == id } ?: return
        audioAssets.value -= existing
    }
}
