package com.alexrdclement.mediaplayground.media.store

interface MediaStoreTransactionRunner {
    suspend fun <T> run(block: suspend MediaStoreTransactionScope.() -> T): T
}
