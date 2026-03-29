package com.alexrdclement.mediaplayground.data.image

import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.Image
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImagesForAlbumFlow(albumId: AlbumId): Flow<List<Image>>
}
