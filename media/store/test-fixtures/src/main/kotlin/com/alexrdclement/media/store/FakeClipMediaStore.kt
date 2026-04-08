package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.store.ClipMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope

class FakeClipMediaStore : ClipMediaStore {

    private val clips = mutableMapOf<ClipId, Clip>()

    override suspend fun get(id: ClipId): Clip? = clips[id]

    override suspend fun getByMediaAssetId(id: MediaAssetId): Clip? =
        clips.values.find { it.mediaAsset.id == id }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(clip: Clip) {
        clips[clip.id] = clip
    }
}
