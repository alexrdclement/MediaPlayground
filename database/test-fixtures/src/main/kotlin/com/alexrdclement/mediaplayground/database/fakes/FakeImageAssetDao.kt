package com.alexrdclement.mediaplayground.database.fakes

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.database.dao.ImageAssetDao
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset
import com.alexrdclement.mediaplayground.database.model.ImageAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class FakeImageAssetDao(
    val mediaAssetDao: FakeMediaAssetDao = FakeMediaAssetDao(),
) : ImageAssetDao {

    val images = MutableStateFlow(emptySet<ImageAsset>())

    private fun buildCompleteImageAsset(imageAsset: ImageAsset): CompleteImageAsset? {
        val mediaAsset = mediaAssetDao.mediaAssets[imageAsset.id] ?: return null
        return CompleteImageAsset(imageAsset = imageAsset, mediaAsset = mediaAsset)
    }

    override suspend fun getImage(id: String): CompleteImageAsset? {
        val imageAsset = images.value.find { it.id == id } ?: return null
        return buildCompleteImageAsset(imageAsset)
    }

    override fun getImageFlow(id: String): Flow<CompleteImageAsset?> {
        return combine(images, mediaAssetDao.mediaAssetsFlow) { files, _ ->
            val imageAsset = files.find { it.id == id } ?: return@combine null
            buildCompleteImageAsset(imageAsset)
        }
    }

    override fun getImagesPagingSource(): PagingSource<Int, CompleteImageAsset> {
        return object : PagingSource<Int, CompleteImageAsset>() {
            override fun getRefreshKey(state: PagingState<Int, CompleteImageAsset>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CompleteImageAsset> {
                val completeFiles = images.value.mapNotNull { buildCompleteImageAsset(it) }
                return LoadResult.Page(
                    data = completeFiles,
                    prevKey = null,
                    nextKey = null,
                )
            }
        }
    }

    override fun getImageCountFlow(): Flow<Int> = images.map { it.size }

    override suspend fun insert(vararg image: ImageAsset) {
        for (newImage in image) {
            if (images.value.any { it.id == newImage.id }) continue
            images.value = images.value + newImage
        }
    }

    override suspend fun update(image: ImageAsset) {
        val existing = images.value.find { it.id == image.id } ?: return
        images.value = images.value - existing + image
    }

    override suspend fun delete(id: String) {
        images.value = images.value.filterNot { it.id == id }.toSet()
    }
}
