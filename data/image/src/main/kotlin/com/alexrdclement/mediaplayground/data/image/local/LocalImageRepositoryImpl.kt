package com.alexrdclement.mediaplayground.data.image.local

import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.Image
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class LocalImageRepositoryImpl @Inject constructor(
    private val localImageDataStore: LocalImageDataStore,
) : LocalImageRepository {

    override fun getImagesForAlbumFlow(albumId: AlbumId): Flow<List<Image>> =
        localImageDataStore.getImagesForAlbumFlow(albumId)
}
