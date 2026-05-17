package com.alexrdclement.mediaplayground.database.transaction

import androidx.room.withTransaction
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase

internal class DatabaseTransactionRunnerImpl(
    private val database: MediaPlaygroundDatabase,
    private val scope: DatabaseTransactionScope,
) : DatabaseTransactionRunner {
    override suspend fun <T> run(block: suspend DatabaseTransactionScope.() -> T): T {
        return database.withTransaction { scope.block() }
    }
}
