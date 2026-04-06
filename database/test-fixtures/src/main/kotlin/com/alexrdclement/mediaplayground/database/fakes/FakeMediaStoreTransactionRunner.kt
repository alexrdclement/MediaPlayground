package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope

class FakeMediaStoreTransactionRunner : MediaStoreTransactionRunner {
    private val scope = object : MediaStoreTransactionScope {}

    override suspend fun <T> run(block: suspend MediaStoreTransactionScope.() -> T): T {
        return scope.block()
    }
}
