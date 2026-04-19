package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.MediaCollectionDao
import com.alexrdclement.mediaplayground.database.model.MediaCollection
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMediaCollectionDao : MediaCollectionDao {

    val mediaCollections = MutableStateFlow(emptySet<MediaCollection>())

    override suspend fun getMediaCollection(id: String): MediaCollection? =
        mediaCollections.value.find { it.id == id }

    override suspend fun insert(vararg mediaCollection: MediaCollection) {
        for (mc in mediaCollection) {
            if (mediaCollections.value.any { it.id == mc.id }) continue
            mediaCollections.value = mediaCollections.value + mc
        }
    }

    override suspend fun update(mediaCollection: MediaCollection) {
        val existing = mediaCollections.value.find { it.id == mediaCollection.id } ?: return
        mediaCollections.value = mediaCollections.value - existing + mediaCollection
    }

    override suspend fun delete(id: String) {
        val existing = mediaCollections.value.find { it.id == id } ?: return
        mediaCollections.value -= existing
    }
}
