package com.alexrdclement.mediaplayground.data.sync

import com.alexrdclement.mediaplayground.database.fakes.FakeMediaAssetSyncStateDao
import com.alexrdclement.mediaplayground.media.model.FakeAudioAsset1
import com.alexrdclement.mediaplayground.media.model.FakeAudioAsset2
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.testing.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LocalMediaAssetSyncStateDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: Fixture

    @BeforeTest
    fun setUp() {
        fixture = Fixture()
    }

    private class Fixture {
        val dao = FakeMediaAssetSyncStateDao()
        val dataStore = LocalMediaAssetSyncStateDataStore(dao)
    }

    @Test
    fun getSyncStateFlow_returnsNull_initially() = runTest {
        val result = fixture.dataStore.getSyncStateFlow(FakeAudioAsset1.id).first()
        assertNull(result)
    }

    @Test
    fun getSyncStateFlow_returnsSyncState_afterUpsert() = runTest {
        fixture.dataStore.upsert(FakeAudioAsset1.id, MediaAssetSyncState.Synced)

        val result = fixture.dataStore.getSyncStateFlow(FakeAudioAsset1.id).first()
        assertEquals(MediaAssetSyncState.Synced, result)
    }

    @Test
    fun getSyncStateFlow_isIndependentPerAsset() = runTest {
        fixture.dataStore.upsert(FakeAudioAsset1.id, MediaAssetSyncState.Synced)
        fixture.dataStore.upsert(FakeAudioAsset2.id, MediaAssetSyncState.Failed)

        assertEquals(MediaAssetSyncState.Synced, fixture.dataStore.getSyncStateFlow(FakeAudioAsset1.id).first())
        assertEquals(MediaAssetSyncState.Failed, fixture.dataStore.getSyncStateFlow(FakeAudioAsset2.id).first())
    }

    @Test
    fun upsert_replacesSyncState() = runTest {
        fixture.dataStore.upsert(FakeAudioAsset1.id, MediaAssetSyncState.Syncing)
        fixture.dataStore.upsert(FakeAudioAsset1.id, MediaAssetSyncState.Synced)

        val result = fixture.dataStore.getSyncStateFlow(FakeAudioAsset1.id).first()
        assertEquals(MediaAssetSyncState.Synced, result)
    }

    @Test
    fun delete_removesSyncState() = runTest {
        fixture.dataStore.upsert(FakeAudioAsset1.id, MediaAssetSyncState.Synced)
        fixture.dataStore.delete(FakeAudioAsset1.id)

        assertNull(fixture.dataStore.getSyncStateFlow(FakeAudioAsset1.id).first())
    }
}
