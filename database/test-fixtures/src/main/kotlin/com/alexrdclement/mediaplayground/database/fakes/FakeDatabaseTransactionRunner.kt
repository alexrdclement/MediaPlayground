package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner

class FakeDatabaseTransactionRunner : DatabaseTransactionRunner {
    override suspend fun <T> run(block: suspend () -> T): T {
        return block()
    }
}
