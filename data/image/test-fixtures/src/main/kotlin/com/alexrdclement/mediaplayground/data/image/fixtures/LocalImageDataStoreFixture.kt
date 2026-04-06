package com.alexrdclement.mediaplayground.data.image.fixtures

import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.data.image.local.LocalImageDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalImageDataStoreFixture(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined),
    val pathProvider: FakePathProvider = FakePathProvider(),
) {
    val transactionScope = FakeDatabaseTransactionScope(coroutineScope)
    val databaseTransactionRunner = FakeDatabaseTransactionRunner(transactionScope)

    val localImageDataStore = LocalImageDataStore(
        imageFileDao = transactionScope.imageFileDao,
        databaseTransactionRunner = databaseTransactionRunner,
        pathProvider = pathProvider,
    )
}
