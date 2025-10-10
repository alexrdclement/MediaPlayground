package com.alexrdclement.mediaplayground.media.session

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.isPlaying
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaSessionState {
    val mediaEngineState: MediaEngineState
}

val MediaSessionState.isPlaying: Flow<Boolean>
    get() = mediaEngineState.isPlaying

val MediaSessionState.loadedMediaItem: Flow<MediaItem?>
    get() = mediaEngineState.getLoadedMediaItem()
