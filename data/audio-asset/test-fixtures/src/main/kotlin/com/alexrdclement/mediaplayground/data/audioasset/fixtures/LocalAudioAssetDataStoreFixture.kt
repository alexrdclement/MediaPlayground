package com.alexrdclement.mediaplayground.data.audioasset.fixtures

import com.alexrdclement.mediaplayground.data.audioasset.local.LocalAudioAssetStore
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalAudioAssetDataStoreFixture(
    val transactionScope: FakeDatabaseTransactionScope = FakeDatabaseTransactionScope(
        CoroutineScope(Dispatchers.Unconfined)
    ),
) {
    val databaseTransactionRunner = FakeDatabaseTransactionRunner(transactionScope)

    val localAudioAssetStore = LocalAudioAssetStore(
        audioAssetDao = transactionScope.audioAssetDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )
}
