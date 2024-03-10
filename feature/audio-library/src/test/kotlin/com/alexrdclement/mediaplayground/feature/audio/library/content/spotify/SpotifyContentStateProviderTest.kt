package com.alexrdclement.mediaplayground.feature.audio.library.content.spotify

import androidx.paging.PagingConfig
import app.cash.turbine.test
import com.alexrdclement.data.audio.test.fixtures.spotify.FakeSpotifyRemoteDataStore
import com.alexrdclement.data.audio.test.fixtures.spotify.auth.FakeSpotifyAuth
import com.alexrdclement.media.session.test.fixtures.FakeMediaSessionManager
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SpotifyContentStateProviderTest {

    private lateinit var spotifyAuth: FakeSpotifyAuth
    private lateinit var spotifyRemoteDataStore: FakeSpotifyRemoteDataStore
    private lateinit var spotifyAudioRepository: SpotifyAudioRepository
    private lateinit var mediaSessionManager: FakeMediaSessionManager

    private lateinit var spotifyContentStateProvider: SpotifyContentStateProvider

    @BeforeTest
    fun setup() {
        spotifyAuth = FakeSpotifyAuth()
        spotifyRemoteDataStore = FakeSpotifyRemoteDataStore()
        spotifyAudioRepository = SpotifyAudioRepositoryImpl(
            spotifyRemoteDataStore = spotifyRemoteDataStore,
        )
        mediaSessionManager = FakeMediaSessionManager()
        spotifyContentStateProvider = SpotifyContentStateProvider(
            spotifyAuth = spotifyAuth,
            spotifyAudioRepository = spotifyAudioRepository,
            mediaSessionManager = mediaSessionManager,
        )
    }

    @Test
    fun notLoggedIn_returnsNotLoggedIn() = runTest {
        spotifyAuth.stubIsLoggedIn(isLoggedIn = false)

        spotifyContentStateProvider.flow(
            coroutineScope = CoroutineScope(this.testScheduler),
            pagingConfig = PagingConfig(pageSize = 10)
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
            pagingConfig = PagingConfig(pageSize = 10)
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
            pagingConfig = PagingConfig(pageSize = 10)
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
