package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId

interface ClipMediaStore {
    suspend fun get(id: ClipId): Clip?

    context(scope: MediaStoreTransactionScope)
    suspend fun put(clip: Clip)
}
