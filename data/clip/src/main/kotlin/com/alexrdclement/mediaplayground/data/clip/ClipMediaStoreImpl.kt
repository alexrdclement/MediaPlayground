package com.alexrdclement.mediaplayground.data.clip

import com.alexrdclement.mediaplayground.data.clip.local.LocalClipDataStore
import com.alexrdclement.mediaplayground.media.model.AudioClip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.store.ClipMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

class ClipMediaStoreImpl @Inject constructor(
    private val localClipDataStore: LocalClipDataStore,
) : ClipMediaStore {

    override suspend fun get(id: ClipId): AudioClip? {
        return localClipDataStore.get(id = id)
    }

    override suspend fun getByMediaAssetId(id: MediaAssetId): AudioClip? {
        return localClipDataStore.getByMediaAssetId(id)
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(clip: AudioClip) =
        localClipDataStore.put(clip)
}
