package com.alexrdclement.mediaplayground.data.mediacollection

import com.alexrdclement.mediaplayground.media.model.MediaCollection
import com.alexrdclement.mediaplayground.media.model.MediaCollectionId
import kotlinx.coroutines.flow.Flow

interface MediaCollectionRepository {
    fun getMediaCollectionFlow(id: MediaCollectionId): Flow<MediaCollection<*>?>
    suspend fun delete(id: MediaCollectionId)
}
