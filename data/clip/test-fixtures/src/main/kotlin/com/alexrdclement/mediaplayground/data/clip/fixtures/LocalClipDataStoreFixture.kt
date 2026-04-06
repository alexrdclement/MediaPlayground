package com.alexrdclement.mediaplayground.data.clip.fixtures

import com.alexrdclement.mediaplayground.data.clip.local.LocalClipDataStore
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.data.mediaasset.fixtures.LocalMediaAssetDataStoreFixture
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaStoreTransactionRunner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalClipDataStoreFixture(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
    val pathProvider: PathProvider = FakePathProvider(),
) {
    val transactionScope: FakeDatabaseTransactionScope = FakeDatabaseTransactionScope(coroutineScope)
    val databaseTransactionRunner: FakeDatabaseTransactionRunner = FakeDatabaseTransactionRunner(transactionScope)
    val transactionRunner: FakeMediaStoreTransactionRunner = FakeMediaStoreTransactionRunner()

    val mediaAssetFixture = LocalMediaAssetDataStoreFixture(transactionScope = transactionScope)

    val localClipDataStore = LocalClipDataStore(
        completeAudioClipDao = transactionScope.completeAudioClipDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )
}
