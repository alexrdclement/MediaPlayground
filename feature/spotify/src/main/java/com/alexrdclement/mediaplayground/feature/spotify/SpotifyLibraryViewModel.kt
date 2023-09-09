package com.alexrdclement.mediaplayground.feature.spotify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import com.alexrdclement.mediaplayground.model.audio.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpotifyLibraryViewModel @Inject constructor(
    private val spotifyAudioRepository: SpotifyAudioRepository,
    private val mediaSessionManager: MediaSessionManager,
) : ViewModel() {

    private val savedTracksPager = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = spotifyAudioRepository::getSavedTracksPagingSource,
    )
    val savedTracks = savedTracksPager.flow.cachedIn(viewModelScope)

    fun onPlayTrack(track: Track) {
        val previewUrl = track.previewUrl ?: return
        val mediaItem = MediaItem.fromUri(previewUrl)
        val player = mediaSessionManager.player.value ?: return
        with(player) {
            setMediaItem(mediaItem)
            play()
        }
    }
}
