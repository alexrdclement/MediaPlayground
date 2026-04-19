package com.alexrdclement.mediaplayground.data.audioasset.local

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

class LocalAudioAssetDataStoreTest {

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
        val localAudioAssetDataStore = LocalAudioAssetDataStore(
            audioAssetDao = transactionScope.audioAssetDao,
            databaseTransactionRunner = databaseTransactionRunner,
        )
    }

    @Test
    fun getAudioAssetFlow_returnsNull_forUnknownAudioAsset() = runTest {
        val result = fixture.localAudioAssetDataStore.getAudioAssetFlow(FakeMediaAsset1.id).first()
        assertNull(result)
    }

    @Test
    fun getAudioAssetFlow_returnsAudioAsset_afterPut() = runTest {
        fixture.transactionRunner.run {
            fixture.localAudioAssetDataStore.put(FakeLocalMediaAsset1)
        }

        val result = fixture.localAudioAssetDataStore.getAudioAssetFlow(FakeLocalMediaAsset1.id).first()
        assertNotNull(result)
        assertEquals(FakeLocalMediaAsset1.id, result.id)
    }

    @Test
    fun delete_removesMediaAsset() = runTest {
        fixture.transactionRunner.run {
            fixture.localAudioAssetDataStore.put(FakeLocalMediaAsset1)
        }

        fixture.transactionRunner.run {
            fixture.localAudioAssetDataStore.delete(FakeLocalMediaAsset1.id)
        }

        val result = fixture.localAudioAssetDataStore.getAudioAssetFlow(FakeLocalMediaAsset1.id).first()
        assertNull(result)
    }

    @Test
    fun getAudioAssetCountFlow_returnsCount() = runTest {
        assertEquals(0, fixture.localAudioAssetDataStore.getAudioAssetCountFlow().first())

        fixture.transactionRunner.run {
            fixture.localAudioAssetDataStore.put(FakeLocalMediaAsset1)
        }

        assertEquals(1, fixture.localAudioAssetDataStore.getAudioAssetCountFlow().first())
    }
}
