package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.AudioClip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.store.ClipMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope

class FakeClipMediaStore : ClipMediaStore {

    private val clips = mutableMapOf<ClipId, AudioClip>()

    override suspend fun get(id: ClipId): AudioClip? = clips[id]

    override suspend fun getByMediaAssetId(id: MediaAssetId): AudioClip? =
        clips.values.find { it.mediaAsset.id == id }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(clip: AudioClip) {
        clips[clip.id] = clip
    }
}
