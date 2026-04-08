package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope

class FakeMediaStoreTransactionRunner : MediaStoreTransactionRunner {
    private val scope = object : MediaStoreTransactionScope {}

    override suspend fun <T> run(block: suspend MediaStoreTransactionScope.() -> T): T =
        scope.block()
}
