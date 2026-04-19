package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.MediaAssetSyncStateDao
import com.alexrdclement.mediaplayground.database.model.MediaAssetSyncStateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeMediaAssetSyncStateDao : MediaAssetSyncStateDao {

    private val _states = MutableStateFlow(emptyMap<String, MediaAssetSyncStateEntity>())

    override fun getFlow(id: String): Flow<MediaAssetSyncStateEntity?> =
        _states.map { it[id] }

    override suspend fun upsert(entity: MediaAssetSyncStateEntity) {
        _states.value = _states.value + (entity.mediaAssetId to entity)
    }

    override suspend fun delete(id: String) {
        _states.value = _states.value - id
    }
}
