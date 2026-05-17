package com.alexrdclement.mediaplayground.data.audioasset.local

import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import com.alexrdclement.mediaplayground.database.model.AudioAssetArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AudioAssetImageCrossRef
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
import kotlin.test.assertTrue

class LocalAudioAssetStoreTest {

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
        val localAudioAssetStore = LocalAudioAssetStore(
            audioAssetDao = transactionScope.audioAssetDao,
            databaseTransactionRunner = databaseTransactionRunner,
        )
    }

    @Test
    fun getAudioAssetFlow_returnsNull_forUnknown() = runTest {
        val result = fixture.localAudioAssetStore.getFlow(FakeMediaAsset1.id).first()
        assertNull(result)
    }

    @Test
    fun getAudioAssetFlow_returns_afterPut() = runTest {
        fixture.transactionRunner.run {
            fixture.localAudioAssetStore.put(FakeLocalMediaAsset1)
        }

        val result = fixture.localAudioAssetStore.getFlow(FakeLocalMediaAsset1.id).first()
        assertNotNull(result)
        assertEquals(FakeLocalMediaAsset1.id, result.id)
    }

    @Test
    fun delete_removesMediaAsset() = runTest {
        fixture.transactionRunner.run {
            fixture.localAudioAssetStore.put(FakeLocalMediaAsset1)
        }

        fixture.transactionRunner.run {
            fixture.localAudioAssetStore.delete(FakeLocalMediaAsset1.id)
        }

        val result = fixture.localAudioAssetStore.getFlow(FakeLocalMediaAsset1.id).first()
        assertNull(result)
    }

    @Test
    fun getCountFlow_returnsCount() = runTest {
        assertEquals(0, fixture.localAudioAssetStore.getCountFlow().first())

        fixture.transactionRunner.run {
            fixture.localAudioAssetStore.put(FakeLocalMediaAsset1)
        }

        assertEquals(1, fixture.localAudioAssetStore.getCountFlow().first())
    }

    @Test
    fun put_withArtist_persistsArtistCrossRef() = runTest {
        fixture.transactionRunner.run {
            fixture.localAudioAssetStore.put(FakeLocalMediaAsset1)
        }

        val expectedArtistId = FakeLocalMediaAsset1.artists.first().id.value
        val expectedCrossRef = AudioAssetArtistCrossRef(
            audioAssetId = FakeLocalMediaAsset1.id.value,
            artistId = expectedArtistId,
        )
        assertTrue(expectedCrossRef in fixture.transactionScope.audioAssetArtistDao.audioAssetArtists)
    }

    @Test
    fun put_withImage_persistsImageCrossRef() = runTest {
        fixture.transactionRunner.run {
            fixture.localAudioAssetStore.put(FakeLocalMediaAsset1)
        }

        val expectedImageId = FakeLocalMediaAsset1.images.first().id.value
        val expectedCrossRef = AudioAssetImageCrossRef(
            audioAssetId = FakeLocalMediaAsset1.id.value,
            imageId = expectedImageId,
        )
        assertTrue(expectedCrossRef in fixture.transactionScope.audioAssetImageDao.audioAssetImages)
    }

    @Test
    fun delete_removesArtistAndImageCrossRefs() = runTest {
        fixture.transactionRunner.run {
            fixture.localAudioAssetStore.put(FakeLocalMediaAsset1)
        }

        fixture.transactionRunner.run {
            fixture.localAudioAssetStore.delete(FakeLocalMediaAsset1.id)
        }

        assertTrue(fixture.transactionScope.audioAssetArtistDao.audioAssetArtists.isEmpty())
        assertTrue(fixture.transactionScope.audioAssetImageDao.audioAssetImages.isEmpty())
    }
}
