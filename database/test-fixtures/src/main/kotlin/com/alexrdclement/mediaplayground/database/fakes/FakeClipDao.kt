package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.ClipDao
import com.alexrdclement.mediaplayground.database.model.Clip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeClipDao : ClipDao {

    val clips = MutableStateFlow(emptySet<Clip>())

    override suspend fun getClip(id: String): Clip? {
        return clips.value.find { it.id == id }
    }

    override fun getClipFlow(id: String): Flow<Clip?> {
        return clips.map { it.find { clip -> clip.id == id } }
    }

    override fun getClipCountFlow(): Flow<Int> = clips.map { it.size }

    override suspend fun insert(vararg clip: Clip) {
        for (newClip in clip) {
            if (clips.value.any { it.id == newClip.id }) continue
            clips.value += newClip
        }
    }

    override suspend fun update(clip: Clip) {
        val existing = clips.value.find { it.id == clip.id } ?: return
        clips.value = clips.value - existing + clip
    }

    override suspend fun getClipIdsByAssetId(assetId: String): List<String> {
        return clips.value.filter { it.assetId == assetId }.map { it.id }
    }

    override suspend fun delete(id: String) {
        val existing = clips.value.find { it.id == id } ?: return
        clips.value -= existing
    }
}
