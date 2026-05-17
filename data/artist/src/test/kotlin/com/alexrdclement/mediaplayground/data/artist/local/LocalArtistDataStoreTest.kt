package com.alexrdclement.mediaplayground.data.artist.local

import com.alexrdclement.mediaplayground.database.mapping.toArtistEntity
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import com.alexrdclement.mediaplayground.media.model.FakeArtist1
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

class LocalArtistDataStoreTest {

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
        val artistDao get() = transactionScope.artistDao
        val localArtistDataStore = LocalArtistDataStore(
            artistDao = transactionScope.artistDao,
            albumArtistDao = transactionScope.albumArtistDao,
            databaseTransactionRunner = databaseTransactionRunner,
        )
    }

    @Test
    fun getArtist_returnsNull_forUnknownId() = runTest {
        val result = fixture.localArtistDataStore.getArtist(FakeArtist1.id)
        assertNull(result)
    }

    @Test
    fun getArtist_returnsArtist_afterInsert() = runTest {
        fixture.artistDao.insert(FakeArtist1.toArtistEntity())

        val result = fixture.localArtistDataStore.getArtist(FakeArtist1.id)
        assertNotNull(result)
        assertEquals(FakeArtist1.id, result.id)
        assertEquals(FakeArtist1.name, result.name)
    }

    @Test
    fun getArtistFlow_emitsArtist_afterInsert() = runTest {
        fixture.artistDao.insert(FakeArtist1.toArtistEntity())

        val result = fixture.localArtistDataStore.getArtistFlow(FakeArtist1.id).first()
        assertNotNull(result)
        assertEquals(FakeArtist1.id, result.id)
        assertEquals(FakeArtist1.name, result.name)
    }

    @Test
    fun getArtistByName_returnsArtist() = runTest {
        fixture.artistDao.insert(FakeArtist1.toArtistEntity())

        val result = fixture.localArtistDataStore.getArtistByName(FakeArtist1.name!!)
        assertNotNull(result)
        assertEquals(FakeArtist1.id, result.id)
        assertEquals(FakeArtist1.name, result.name)
    }

    @Test
    fun getArtistByName_returnsNull_forUnknown() = runTest {
        val result = fixture.localArtistDataStore.getArtistByName("Unknown Artist")
        assertNull(result)
    }

    @Test
    fun updateArtistName_updatesName() = runTest {
        fixture.artistDao.insert(FakeArtist1.toArtistEntity())

        fixture.localArtistDataStore.updateArtistName(FakeArtist1.id, "New Name")

        val result = fixture.localArtistDataStore.getArtist(FakeArtist1.id)
        assertNotNull(result)
        assertEquals("New Name", result.name)
    }

    @Test
    fun updateArtistNotes_updatesNotes() = runTest {
        fixture.artistDao.insert(FakeArtist1.toArtistEntity())

        fixture.localArtistDataStore.updateArtistNotes(FakeArtist1.id, "New notes")

        val result = fixture.localArtistDataStore.getArtist(FakeArtist1.id)
        assertNotNull(result)
        assertEquals("New notes", result.notes)
    }
}
