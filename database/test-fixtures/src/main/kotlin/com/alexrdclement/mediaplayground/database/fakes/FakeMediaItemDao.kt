package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.MediaItemDao
import com.alexrdclement.mediaplayground.database.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMediaItemDao : MediaItemDao {

    private val _items = MutableStateFlow(emptyMap<String, MediaItem>())

    override suspend fun getMediaItem(id: String): MediaItem? = _items.value[id]

    override suspend fun insert(vararg mediaItem: MediaItem) {
        val current = _items.value.toMutableMap()
        for (item in mediaItem) {
            if (!current.containsKey(item.id)) {
                current[item.id] = item
            }
        }
        _items.value = current
    }

    override suspend fun delete(id: String) {
        _items.value = _items.value - id
    }
}
