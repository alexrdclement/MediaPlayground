package com.alexrdclement.mediaplayground.data.clip.local

import com.alexrdclement.mediaplayground.data.clip.fixtures.LocalClipDataStoreFixture
import com.alexrdclement.mediaplayground.media.model.FakeClip1
import com.alexrdclement.mediaplayground.media.model.FakeLocalClip1
import com.alexrdclement.mediaplayground.media.model.FakeLocalMediaAsset1
import com.alexrdclement.testing.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class LocalClipDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: LocalClipDataStoreFixture

    @BeforeTest
    fun setUp() {
        fixture = LocalClipDataStoreFixture()
    }

    @Test
    fun getClipFlow_returnsNull_forUnknownClip() = runTest {
        val result = fixture.localClipDataStore.getClipFlow(FakeClip1.id).first()
        assertNull(result)
    }

    @Test
    fun getClipFlow_returnsClip_afterPut() = runTest {
        fixture.transactionRunner.run {
            fixture.mediaAssetFixture.localMediaAssetDataStore.put(FakeLocalMediaAsset1)
            fixture.localClipDataStore.put(FakeLocalClip1)
        }

        val result = fixture.localClipDataStore.getClipFlow(FakeLocalClip1.id).first()
        assertNotNull(result)
        assertEquals(FakeLocalClip1.id, result.id)
    }

    @Test
    fun delete_removesClip() = runTest {
        fixture.transactionRunner.run {
            fixture.mediaAssetFixture.localMediaAssetDataStore.put(FakeLocalMediaAsset1)
            fixture.localClipDataStore.put(FakeLocalClip1)
        }

        fixture.transactionRunner.run {
            fixture.localClipDataStore.delete(FakeLocalClip1.id)
        }

        val result = fixture.localClipDataStore.getClipFlow(FakeLocalClip1.id).first()
        assertNull(result)
    }

    @Test
    fun updateClipTitle_updatesTitle() = runTest {
        fixture.transactionRunner.run {
            fixture.mediaAssetFixture.localMediaAssetDataStore.put(FakeLocalMediaAsset1)
            fixture.localClipDataStore.put(FakeLocalClip1)
        }

        fixture.localClipDataStore.updateClipTitle(FakeLocalClip1.id, "New Title")

        val clip = fixture.transactionScope.clipDao.getClip(FakeLocalClip1.id.value)
        assertNotNull(clip)
        assertEquals("New Title", clip.title)
    }

    @Test
    fun getClipCountFlow_returnsCount() = runTest {
        assertEquals(0, fixture.localClipDataStore.getClipCountFlow().first())

        fixture.transactionRunner.run {
            fixture.mediaAssetFixture.localMediaAssetDataStore.put(FakeLocalMediaAsset1)
            fixture.localClipDataStore.put(FakeLocalClip1)
        }

        assertEquals(1, fixture.localClipDataStore.getClipCountFlow().first())
    }
}
