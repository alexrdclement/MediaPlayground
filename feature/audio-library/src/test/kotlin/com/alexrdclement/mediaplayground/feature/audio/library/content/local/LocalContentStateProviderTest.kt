package com.alexrdclement.mediaplayground.feature.audio.library.content.local

import androidx.paging.PagingConfig
import app.cash.turbine.test
import com.alexrdclement.data.audio.test.fixtures.local.LocalAudioRepositoryFixture
import com.alexrdclement.media.session.test.fixtures.FakeMediaSessionManager
import com.alexrdclement.mediaplayground.ui.shared.util.PreviewTracks1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalContentStateProviderTest {

    private lateinit var localAudioRepositoryFixture: LocalAudioRepositoryFixture
    private lateinit var mediaSessionManager: FakeMediaSessionManager

    private lateinit var localContentStateProvider: LocalContentStateProvider

    @BeforeTest
    fun setup() {
        localAudioRepositoryFixture = LocalAudioRepositoryFixture()
        mediaSessionManager = FakeMediaSessionManager()
        localContentStateProvider = LocalContentStateProvider(
            localAudioRepository = localAudioRepositoryFixture.localAudioRepository,
            mediaSessionManager = mediaSessionManager,
        )
    }

    @Test
    fun noTracks_returnsEmpty() = runTest {
        localContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = PagingConfig(pageSize = 10)
        ).test {
            assertTrue(awaitItem() is LocalContentState.Empty)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun oneTracks_returnsContent() = runTest {
        localAudioRepositoryFixture.stubTracks(PreviewTracks1)

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
        localAudioRepositoryFixture.stubTracks(PreviewTracks1)

        localContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = PagingConfig(pageSize = 10)
        ).test {
            val firstItem = awaitItem()
            assertTrue(firstItem is LocalContentState.Content)

            localAudioRepositoryFixture.stubTracks(listOf())
            assertTrue(awaitItem() is LocalContentState.Empty)

            localAudioRepositoryFixture.stubTracks(PreviewTracks1)

            val lastItem = awaitItem()
            assertTrue(lastItem is LocalContentState.Content)

            assertEquals(firstItem.tracks, lastItem.tracks)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
