package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.MediaItemImageDao
import com.alexrdclement.mediaplayground.database.model.ImageAsset
import com.alexrdclement.mediaplayground.database.model.MediaItemImageCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class FakeMediaItemImageDao(
    private val imageDao: FakeImageAssetDao = FakeImageAssetDao(),
) : MediaItemImageDao {

    val crossRefs = mutableSetOf<MediaItemImageCrossRef>()
    private val crossRefsFlow = MutableStateFlow(emptySet<MediaItemImageCrossRef>())

    override suspend fun insert(vararg crossRef: MediaItemImageCrossRef) {
        crossRefs.addAll(crossRef)
        crossRefsFlow.value = crossRefsFlow.value + crossRef.toSet()
    }

    override suspend fun delete(crossRef: MediaItemImageCrossRef) {
        crossRefs.remove(crossRef)
        crossRefsFlow.value = crossRefsFlow.value - crossRef
    }

    override suspend fun deleteForItem(itemId: String) {
        crossRefs.removeAll { it.itemId == itemId }
        crossRefsFlow.value = crossRefsFlow.value.filterNot { it.itemId == itemId }.toSet()
    }

    override fun getImagesForItemFlow(itemId: String): Flow<List<ImageAsset>> {
        return combine(crossRefsFlow, imageDao.images) { refs, images ->
            val imageIds = refs.filter { it.itemId == itemId }.map { it.imageAssetId }.toSet()
            images.filter { it.id in imageIds }
        }
    }
}
