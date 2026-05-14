package com.alexrdclement.mediaplayground.data.artist.fixtures

import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalArtistDataStoreFixture(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Unconfined),
) {
    val transactionScope = FakeDatabaseTransactionScope(coroutineScope)
    val databaseTransactionRunner = FakeDatabaseTransactionRunner(transactionScope)

    val localArtistDataStore = LocalArtistDataStore(
        artistDao = transactionScope.artistDao,
        albumArtistDao = transactionScope.albumArtistDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )
}
