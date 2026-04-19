package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.MediaAssetDao
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeMediaAssetDao : MediaAssetDao {

    private val _mediaAssets = MutableStateFlow(emptyMap<String, MediaAsset>())
    val mediaAssetsFlow = _mediaAssets.asStateFlow()
    val mediaAssets: Map<String, MediaAsset> get() = _mediaAssets.value

    override suspend fun getMediaAsset(id: String): MediaAsset? = _mediaAssets.value[id]

    override suspend fun insert(vararg mediaAsset: MediaAsset) {
        val current = _mediaAssets.value.toMutableMap()
        for (asset in mediaAsset) {
            if (current.containsKey(asset.id)) continue
            current[asset.id] = asset
        }
        _mediaAssets.value = current
    }

    override suspend fun delete(id: String) {
        _mediaAssets.value = _mediaAssets.value - id
    }
}
