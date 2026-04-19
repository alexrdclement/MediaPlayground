package com.alexrdclement.mediaplayground.data.clip.fixtures

import com.alexrdclement.mediaplayground.data.clip.local.LocalClipDataStore
import com.alexrdclement.mediaplayground.data.audioasset.fixtures.LocalAudioAssetDataStoreFixture
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import com.alexrdclement.media.store.FakeMediaStoreTransactionRunner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalClipDataStoreFixture(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
) {
    val transactionScope: FakeDatabaseTransactionScope = FakeDatabaseTransactionScope(coroutineScope)
    val databaseTransactionRunner: FakeDatabaseTransactionRunner = FakeDatabaseTransactionRunner(transactionScope)
    val transactionRunner: FakeMediaStoreTransactionRunner = FakeMediaStoreTransactionRunner()

    val mediaAssetFixture = LocalAudioAssetDataStoreFixture(transactionScope = transactionScope)

    val localClipDataStore = LocalClipDataStore(
        completeAudioClipDao = transactionScope.completeAudioClipDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )
}
