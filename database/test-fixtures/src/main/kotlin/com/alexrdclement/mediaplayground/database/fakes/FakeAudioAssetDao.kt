package com.alexrdclement.mediaplayground.database.fakes

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.database.dao.AudioAssetDao
import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.CompleteAudioAsset
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class FakeAudioAssetDao(
    val mediaAssetDao: FakeMediaAssetDao = FakeMediaAssetDao(),
    val audioAssetArtistDao: FakeAudioAssetArtistDao? = null,
    val artistDao: FakeArtistDao? = null,
    val mediaItemImageDao: FakeMediaItemImageDao? = null,
    val imageAssetDao: FakeImageAssetDao? = null,
    val mediaItemDao: FakeMediaItemDao = FakeMediaItemDao(),
) : AudioAssetDao {

    val audioAssets = MutableStateFlow(emptySet<AudioAsset>())

    internal fun buildCompleteAudioAsset(audioAsset: AudioAsset): CompleteAudioAsset? {
        val mediaItem = mediaItemDao.getMediaItemSync(audioAsset.id) ?: return null
        val mediaAsset = mediaAssetDao.mediaAssets[audioAsset.id] ?: return null
        val artists = if (audioAssetArtistDao != null && artistDao != null) {
            audioAssetArtistDao.audioAssetArtists
                .filter { it.audioAssetId == audioAsset.id }
                .mapNotNull { ref -> artistDao.artists.value.find { it.id == ref.artistId } }
        } else emptyList()
        val images = if (mediaItemImageDao != null && imageAssetDao != null) {
            mediaItemImageDao.crossRefs
                .filter { it.itemId == audioAsset.id }
                .mapNotNull { ref ->
                    val img = imageAssetDao.images.value.find { it.id == ref.imageAssetId } ?: return@mapNotNull null
                    val mediaAssetImg = mediaAssetDao.mediaAssets[img.id] ?: return@mapNotNull null
                    val imgMediaItem = mediaItemDao.getMediaItemSync(img.id) ?: return@mapNotNull null
                    CompleteImageAsset(imageAsset = img, mediaItem = imgMediaItem, mediaAsset = mediaAssetImg)
                }
        } else emptyList()
        return CompleteAudioAsset(
            audioAsset = audioAsset,
            mediaItem = mediaItem,
            mediaAsset = mediaAsset,
            artists = artists,
            images = images,
        )
    }

    override suspend fun getAudioAsset(id: String): CompleteAudioAsset? {
        val audioAsset = audioAssets.value.find { it.id == id } ?: return null
        return buildCompleteAudioAsset(audioAsset)
    }

    override suspend fun getAudioAssetByFileName(fileName: String): CompleteAudioAsset? {
        val mediaAsset = mediaAssetDao.mediaAssets.values.find { it.fileName == fileName } ?: return null
        val audioAsset = audioAssets.value.find { it.id == mediaAsset.id } ?: return null
        return buildCompleteAudioAsset(audioAsset)
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
