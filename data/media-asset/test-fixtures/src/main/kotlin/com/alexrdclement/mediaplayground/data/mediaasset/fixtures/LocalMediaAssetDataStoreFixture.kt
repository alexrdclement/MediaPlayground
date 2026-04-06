package com.alexrdclement.mediaplayground.data.mediaasset.fixtures

import com.alexrdclement.mediaplayground.data.mediaasset.local.LocalMediaAssetDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalMediaAssetDataStoreFixture(
    val transactionScope: FakeDatabaseTransactionScope = FakeDatabaseTransactionScope(
        CoroutineScope(Dispatchers.Unconfined)
    ),
) {
    val databaseTransactionRunner = FakeDatabaseTransactionRunner(transactionScope)

    val localMediaAssetDataStore = LocalMediaAssetDataStore(
        audioFileDao = transactionScope.audioFileDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )
}
