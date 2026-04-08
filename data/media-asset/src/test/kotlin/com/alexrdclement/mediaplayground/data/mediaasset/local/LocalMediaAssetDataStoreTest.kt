package com.alexrdclement.mediaplayground.data.mediaasset.local

import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import com.alexrdclement.media.store.FakeMediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.mediaplayground.media.model.FakeMediaAsset1
import com.alexrdclement.testing.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class LocalMediaAssetDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: Fixture

    @BeforeTest
    fun setUp() {
        fixture = Fixture()
    }

    private class Fixture {
        val transactionScope = FakeDatabaseTransactionScope(CoroutineScope(Dispatchers.Unconfined))
        val databaseTransactionRunner = FakeDatabaseTransactionRunner(transactionScope)
        val transactionRunner = FakeMediaStoreTransactionRunner()
        val localMediaAssetDataStore = LocalMediaAssetDataStore(
            audioFileDao = transactionScope.audioFileDao,
            databaseTransactionRunner = databaseTransactionRunner,
        )
    }

    @Test
    fun getMediaAssetFlow_returnsNull_forUnknownMediaAsset() = runTest {
        val result = fixture.localMediaAssetDataStore.getMediaAssetFlow(FakeMediaAsset1.id).first()
        assertNull(result)
    }

    @Test
    fun getMediaAssetFlow_returnsMediaAsset_afterPut() = runTest {
        fixture.transactionRunner.run {
            fixture.localMediaAssetDataStore.put(FakeLocalMediaAsset1)
        }

        val result = fixture.localMediaAssetDataStore.getMediaAssetFlow(FakeLocalMediaAsset1.id).first()
        assertNotNull(result)
        assertEquals(FakeLocalMediaAsset1.id, result.id)
    }

    @Test
    fun delete_removesMediaAsset() = runTest {
        fixture.transactionRunner.run {
            fixture.localMediaAssetDataStore.put(FakeLocalMediaAsset1)
        }

        fixture.transactionRunner.run {
            fixture.localMediaAssetDataStore.delete(FakeLocalMediaAsset1.id)
        }

        val result = fixture.localMediaAssetDataStore.getMediaAssetFlow(FakeLocalMediaAsset1.id).first()
        assertNull(result)
    }

    @Test
    fun getMediaAssetCountFlow_returnsCount() = runTest {
        assertEquals(0, fixture.localMediaAssetDataStore.getMediaAssetCountFlow().first())

        fixture.transactionRunner.run {
            fixture.localMediaAssetDataStore.put(FakeLocalMediaAsset1)
        }

        assertEquals(1, fixture.localMediaAssetDataStore.getMediaAssetCountFlow().first())
    }
}
