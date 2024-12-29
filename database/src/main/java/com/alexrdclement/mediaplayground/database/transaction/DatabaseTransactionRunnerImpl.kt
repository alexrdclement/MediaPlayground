package com.alexrdclement.mediaplayground.database.transaction

import androidx.room.withTransaction
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase

internal class DatabaseTransactionRunnerImpl(
    private val database: MediaPlaygroundDatabase
) : DatabaseTransactionRunner {
    override suspend fun <T> run(block: suspend () -> T): T {
        return database.withTransaction(block)
    }
}
