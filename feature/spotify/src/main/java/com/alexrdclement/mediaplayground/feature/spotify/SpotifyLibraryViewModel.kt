package com.alexrdclement.mediaplayground.feature.spotify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import com.alexrdclement.mediaplayground.mediasession.mapper.toMediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpotifyLibraryViewModel @Inject constructor(
    private val spotifyAuth: SpotifyAuth,
    private val spotifyAudioRepository: SpotifyAudioRepository,
    private val mediaSessionManager: MediaSessionManager,
) : ViewModel() {

    val isLoggedIn = spotifyAuth.isLoggedIn

    private val savedTracksPager = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = spotifyAudioRepository::getSavedTracksPagingSource,
    )
    val savedTracks = savedTracksPager.flow.cachedIn(viewModelScope)

    fun onLogOutClick() {
        spotifyAuth.logOut()
    }

    fun onPlayTrack(track: Track) {
        val mediaItem = track.toMediaItem()
        val player = mediaSessionManager.player.value ?: return
        with(player) {
            setMediaItem(mediaItem)
            play()
        }
    }
}
