package com.alexrdclement.mediaplayground.database.fakes

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.database.model.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeImageDao : ImageDao {

    val images = MutableStateFlow(emptySet<Image>())

    override suspend fun getImage(id: String): Image? {
        return images.value.find { it.id == id }
    }

    override fun getImageFlow(id: String): Flow<Image?> {
        return images.map { it.find { image -> image.id == id } }
    }

    override suspend fun insert(vararg image: Image) {
        for (newImage in image) {
            if (images.value.any { it.id == newImage.id }) continue
            images.value = images.value + newImage
        }
    }

    override suspend fun update(image: Image) {
        val existing = images.value.find { it.id == image.id } ?: return
        images.value = images.value - existing + image
    }

    override fun getImagesPagingSource(): PagingSource<Int, Image> {
        return object : PagingSource<Int, Image>() {
            override fun getRefreshKey(state: PagingState<Int, Image>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
                return LoadResult.Page(
                    data = images.value.toList(),
                    prevKey = null,
                    nextKey = null,
                )
            }
        }
    }

    override fun getImageCountFlow(): Flow<Int> = images.map { it.size }

    override suspend fun delete(id: String) {
        images.value = images.value.filterNot { it.id == id }.toSet()
    }
}
