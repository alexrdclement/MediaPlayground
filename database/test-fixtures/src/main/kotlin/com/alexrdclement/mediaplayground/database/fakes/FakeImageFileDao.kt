package com.alexrdclement.mediaplayground.database.fakes

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexrdclement.mediaplayground.database.dao.ImageFileDao
import com.alexrdclement.mediaplayground.database.model.ImageFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeImageFileDao : ImageFileDao {

    val images = MutableStateFlow(emptySet<ImageFile>())

    override suspend fun getImage(id: String): ImageFile? {
        return images.value.find { it.id == id }
    }

    override fun getImageFlow(id: String): Flow<ImageFile?> {
        return images.map { it.find { image -> image.id == id } }
    }

    override suspend fun insert(vararg image: ImageFile) {
        for (newImage in image) {
            if (images.value.any { it.id == newImage.id }) continue
            images.value = images.value + newImage
        }
    }

    override suspend fun update(image: ImageFile) {
        val existing = images.value.find { it.id == image.id } ?: return
        images.value = images.value - existing + image
    }

    override fun getImagesPagingSource(): PagingSource<Int, ImageFile> {
        return object : PagingSource<Int, ImageFile>() {
            override fun getRefreshKey(state: PagingState<Int, ImageFile>): Int? = null
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageFile> {
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
