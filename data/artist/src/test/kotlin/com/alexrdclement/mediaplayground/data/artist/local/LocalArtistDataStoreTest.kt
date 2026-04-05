package com.alexrdclement.mediaplayground.data.artist.local

import com.alexrdclement.mediaplayground.data.artist.local.mapper.toArtistEntity
import com.alexrdclement.mediaplayground.database.fakes.FakeArtistDao
import com.alexrdclement.mediaplayground.media.model.FakeSimpleArtist1
import com.alexrdclement.testing.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class LocalArtistDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: Fixture

    @BeforeTest
    fun setUp() {
        fixture = Fixture()
    }

    private class Fixture {
        val artistDao = FakeArtistDao()
        val localArtistDataStore = LocalArtistDataStore(artistDao)
    }

    @Test
    fun getArtist_returnsNull_forUnknownId() = runTest {
        val result = fixture.localArtistDataStore.getArtist(FakeSimpleArtist1.id)
        assertNull(result)
    }

    @Test
    fun getArtist_returnsArtist_afterInsert() = runTest {
        fixture.artistDao.insert(FakeSimpleArtist1.toArtistEntity())

        val result = fixture.localArtistDataStore.getArtist(FakeSimpleArtist1.id)
        assertNotNull(result)
        assertEquals(FakeSimpleArtist1.id, result.id)
        assertEquals(FakeSimpleArtist1.name, result.name)
    }

    @Test
    fun getArtistFlow_emitsArtist_afterInsert() = runTest {
        fixture.artistDao.insert(FakeSimpleArtist1.toArtistEntity())

        val result = fixture.localArtistDataStore.getArtistFlow(FakeSimpleArtist1.id).first()
        assertNotNull(result)
        assertEquals(FakeSimpleArtist1.id, result.id)
        assertEquals(FakeSimpleArtist1.name, result.name)
    }

    @Test
    fun getArtistByName_returnsArtist() = runTest {
        fixture.artistDao.insert(FakeSimpleArtist1.toArtistEntity())

        val result = fixture.localArtistDataStore.getArtistByName(FakeSimpleArtist1.name!!)
        assertNotNull(result)
        assertEquals(FakeSimpleArtist1.id, result.id)
        assertEquals(FakeSimpleArtist1.name, result.name)
    }

    @Test
    fun getArtistByName_returnsNull_forUnknown() = runTest {
        val result = fixture.localArtistDataStore.getArtistByName("Unknown Artist")
        assertNull(result)
    }

    @Test
    fun updateArtistName_updatesName() = runTest {
        fixture.artistDao.insert(FakeSimpleArtist1.toArtistEntity())

        fixture.localArtistDataStore.updateArtistName(FakeSimpleArtist1.id, "New Name")

        val result = fixture.localArtistDataStore.getArtist(FakeSimpleArtist1.id)
        assertNotNull(result)
        assertEquals("New Name", result.name)
    }

    @Test
    fun updateArtistNotes_updatesNotes() = runTest {
        fixture.artistDao.insert(FakeSimpleArtist1.toArtistEntity())

        fixture.localArtistDataStore.updateArtistNotes(FakeSimpleArtist1.id, "New notes")

        val result = fixture.localArtistDataStore.getArtist(FakeSimpleArtist1.id)
        assertNotNull(result)
        assertEquals("New notes", result.notes)
    }
}
