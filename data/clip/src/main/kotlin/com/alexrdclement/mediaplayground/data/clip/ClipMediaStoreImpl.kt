package com.alexrdclement.mediaplayground.data.clip

import com.alexrdclement.mediaplayground.data.clip.local.LocalClipDataStore
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.store.ClipMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

class ClipMediaStoreImpl @Inject constructor(
    private val localClipDataStore: LocalClipDataStore,
) : ClipMediaStore {

    override suspend fun get(id: ClipId): Clip? {
        return localClipDataStore.get(id = id)
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(clip: Clip) =
        localClipDataStore.put(clip)
}
