package com.alexrdclement.mediaplayground.feature.audio.library.content.local

import androidx.paging.PagingConfig
import app.cash.turbine.test
import com.alexrdclement.media.ui.fakes.FakeMediaSessionState
import com.alexrdclement.mediaplayground.data.audio.local.fixtures.LocalAudioRepositoryFixture
import com.alexrdclement.mediaplayground.ui.util.PreviewTracks1
import com.alexrdclement.testing.MainDispatcherRule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalContentStateProviderTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var localAudioRepositoryFixture: LocalAudioRepositoryFixture
    private lateinit var mediaSessionState: FakeMediaSessionState

    private lateinit var localContentStateProvider: LocalContentStateProvider

    @BeforeTest
    fun setup() {
        localAudioRepositoryFixture = LocalAudioRepositoryFixture()
        mediaSessionState = FakeMediaSessionState()
        localContentStateProvider = LocalContentStateProvider(
            localAudioRepository = localAudioRepositoryFixture.localAudioRepository,
            mediaSessionState = mediaSessionState,
        )
    }

    @Test
    fun noTracksOrAlbums_returnsEmpty() = runTest {
        localContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = PagingConfig(pageSize = 10)
        ).test {
            assertTrue(awaitItem() is LocalContentState.Empty)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun nonEmptyTracksAndAlbums_returnsContent() = runTest {
        // Stubbing tracks also stubs albums
        localAudioRepositoryFixture.putTracks(PreviewTracks1)

        localContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = PagingConfig(pageSize = 10)
        ).test {
            assertTrue(awaitItem() is LocalContentState.Content)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun doesNotRecreatePagingDataFlowsOnStateChange() = runTest {
        val tracks = PreviewTracks1

        // Stubbing tracks also stubs albums
        localAudioRepositoryFixture.putTracks(tracks)

        localContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = PagingConfig(pageSize = 10)
        ).test {
            val firstItem = awaitItem()
            assertTrue(firstItem is LocalContentState.Content)

            localAudioRepositoryFixture.deleteTracks(tracks)
            assertTrue(awaitItem() is LocalContentState.Empty)

            localAudioRepositoryFixture.putTracks(tracks)

            val lastItem = awaitItem()
            assertTrue(lastItem is LocalContentState.Content)

            assertEquals(firstItem.tracks, lastItem.tracks)
            assertEquals(firstItem.albums, lastItem.albums)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
