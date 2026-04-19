package com.alexrdclement.mediaplayground.media.mediaimport.util

import com.alexrdclement.media.store.FakeMediaAssetSyncStateStore
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SyncStateTrackingTest {

    private val syncStateStore = FakeMediaAssetSyncStateStore()
    private val transactionRunner = ImmediateTransactionRunner()
    private val id = AudioAssetId("test-id")

    @Test
    fun runTracked_setsStateSynced_onSuccess() = runTest {
        syncStateStore.runTracked<String, String>(id, transactionRunner) {
            Result.Success("ok")
        }

        assertEquals(MediaAssetSyncState.Synced, syncStateStore.states[id])
    }

    @Test
    fun runTracked_setsStateFailed_whenBlockReturnsFailure() = runTest {
        syncStateStore.runTracked<String, String>(id, transactionRunner) {
            Result.Failure("error")
        }

        assertEquals(MediaAssetSyncState.Failed, syncStateStore.states[id])
    }

    @Test
    fun runTracked_setsStateFailed_whenBlockThrows() = runTest {
        try {
            syncStateStore.runTracked<String, String>(id, transactionRunner) {
                throw RuntimeException("boom")
            }
        } catch (_: RuntimeException) {}

        assertEquals(MediaAssetSyncState.Failed, syncStateStore.states[id])
    }

    @Test
    fun runTracked_setsStateFailed_whenCoroutineIsCancelled() = runTest {
        val blockStarted = CompletableDeferred<Unit>()
        val suspendingRunner = SuspendingTransactionRunner(blockStarted)

        val job = launch {
            syncStateStore.runTracked<String, String>(id, suspendingRunner) {
                Result.Success("ok")
            }
        }

        blockStarted.await()
        job.cancelAndJoin()

        assertEquals(MediaAssetSyncState.Failed, syncStateStore.states[id])
    }
}

private class ImmediateTransactionRunner : MediaStoreTransactionRunner {
    private val scope = object : MediaStoreTransactionScope {}
    override suspend fun <T> run(block: suspend MediaStoreTransactionScope.() -> T): T = scope.block()
}

private class SuspendingTransactionRunner(
    private val started: CompletableDeferred<Unit>,
) : MediaStoreTransactionRunner {
    override suspend fun <T> run(block: suspend MediaStoreTransactionScope.() -> T): T =
        CompletableDeferred<T>().also { started.complete(Unit) }.await()
}
