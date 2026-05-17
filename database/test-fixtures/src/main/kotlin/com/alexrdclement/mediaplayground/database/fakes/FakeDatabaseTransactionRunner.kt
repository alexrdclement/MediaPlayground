package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionScope

class FakeDatabaseTransactionRunner(
    private val scope: DatabaseTransactionScope,
) : DatabaseTransactionRunner {
    override suspend fun <T> run(block: suspend DatabaseTransactionScope.() -> T): T {
        return scope.block()
    }
}
