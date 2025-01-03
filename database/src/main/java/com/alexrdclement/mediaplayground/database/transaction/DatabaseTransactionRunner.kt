package com.alexrdclement.mediaplayground.database.transaction

interface DatabaseTransactionRunner {
    suspend fun <T> run(block: suspend () -> T): T
}
