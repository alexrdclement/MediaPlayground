package com.alexrdclement.mediaplayground.feature.audio.library.content.spotify

import androidx.paging.PagingConfig
import app.cash.turbine.test
import com.alexrdclement.media.ui.fakes.FakeMediaSessionState
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.FakeSpotifyAuth
import com.alexrdclement.mediaplayground.data.audio.spotify.fixtures.SpotifyAudioRepositoryFixture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SpotifyContentStateProviderTest {

    private lateinit var spotifyAuth: FakeSpotifyAuth
    private lateinit var spotifyAudioRepositoryFixture: SpotifyAudioRepositoryFixture
    private lateinit var mediaSessionState: FakeMediaSessionState

    private lateinit var spotifyContentStateProvider: SpotifyContentStateProvider

    private val pagingConfig = PagingConfig(pageSize = 10)

    @BeforeTest
    fun setup() {
        spotifyAuth = FakeSpotifyAuth()
        spotifyAudioRepositoryFixture = SpotifyAudioRepositoryFixture()
        mediaSessionState = FakeMediaSessionState()
        spotifyContentStateProvider = SpotifyContentStateProvider(
            spotifyAuth = spotifyAuth,
            spotifyAudioRepository = spotifyAudioRepositoryFixture.spotifyAudioRepository,
            mediaSessionState = mediaSessionState,
        )
    }

    @Test
    fun notLoggedIn_returnsNotLoggedIn() = runTest {
        spotifyAuth.stubIsLoggedIn(isLoggedIn = false)

        spotifyContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = pagingConfig,
        ).test {
            assertEquals(SpotifyContentState.NotLoggedIn, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun loggedIn_returnsLoggedIn() = runTest {
        spotifyAuth.stubIsLoggedIn(isLoggedIn = true)

        spotifyContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = pagingConfig,
        ).test {
            assertTrue(awaitItem() is SpotifyContentState.LoggedIn)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun doesNotRecreatePagingDataFlowsOnStateChange() = runTest {
        spotifyAuth.stubIsLoggedIn(isLoggedIn = true)

        spotifyContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = pagingConfig,
        ).test {
            val firstItem = awaitItem()
            assertTrue(firstItem is SpotifyContentState.LoggedIn)

            spotifyAuth.stubIsLoggedIn(isLoggedIn = false)
            assertTrue(awaitItem() is SpotifyContentState.NotLoggedIn)

            spotifyAuth.stubIsLoggedIn(isLoggedIn = true)
            val lastItem = awaitItem()
            assertTrue(lastItem is SpotifyContentState.LoggedIn)

            assertEquals(firstItem.savedTracks, lastItem.savedTracks)
            assertEquals(firstItem.savedAlbums, lastItem.savedAlbums)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
