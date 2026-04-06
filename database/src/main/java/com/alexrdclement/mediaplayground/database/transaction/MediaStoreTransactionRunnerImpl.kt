package com.alexrdclement.mediaplayground.database.transaction

import androidx.room.withTransaction
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope

internal class MediaStoreTransactionRunnerImpl(
    private val database: MediaPlaygroundDatabase,
) : MediaStoreTransactionRunner {
    private val scope = object : MediaStoreTransactionScope {}

    override suspend fun <T> run(block: suspend MediaStoreTransactionScope.() -> T): T {
        return database.withTransaction { scope.block() }
    }
}
