package com.alexrdclement.mediaplayground.data.sync

import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

@Inject
class MediaStoreTransactionRunnerImpl(
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) : MediaStoreTransactionRunner {
    private val scope = object : MediaStoreTransactionScope {}

    override suspend fun <T> run(block: suspend MediaStoreTransactionScope.() -> T): T =
        databaseTransactionRunner.run { scope.block() }
}
