package com.alexrdclement.mediaplayground.data.image.local

import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.data.image.local.mapper.toImageEntity
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.database.fakes.FakeImageDao
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeImage1
import com.alexrdclement.mediaplayground.model.audio.fakes.FakeLocalSimpleAlbum1
import com.alexrdclement.testing.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LocalImageDataStoreTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fixture: Fixture

    @BeforeTest
    fun setUp() {
        fixture = Fixture()
    }

    private class Fixture {
        val imageDao = FakeImageDao()
        val pathProvider: PathProvider = FakePathProvider()
        val localImageDataStore = LocalImageDataStore(imageDao, pathProvider)
    }

    @Test
    fun getImagesForAlbumFlow_returnsEmpty_forUnknownAlbum() = runTest {
        val result = fixture.localImageDataStore.getImagesForAlbumFlow(FakeLocalSimpleAlbum1.id).first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun getImagesForAlbumFlow_returnsImages_afterInsert() = runTest {
        val albumId = FakeLocalSimpleAlbum1.id
        fixture.imageDao.insert(FakeImage1.toImageEntity(albumId))

        val result = fixture.localImageDataStore.getImagesForAlbumFlow(albumId).first()
        assertEquals(1, result.size)
        val image = result.first()
        assertNotNull(image.uri)
    }
}
