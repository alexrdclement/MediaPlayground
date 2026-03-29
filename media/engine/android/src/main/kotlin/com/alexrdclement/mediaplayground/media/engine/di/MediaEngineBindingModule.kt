package com.alexrdclement.mediaplayground.media.engine.di

import com.alexrdclement.mediaplayground.media.engine.AndroidMediaEngineControl
import com.alexrdclement.mediaplayground.media.engine.AndroidMediaEngineState
import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl
import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.PlayheadControl
import com.alexrdclement.mediaplayground.media.engine.PlayheadControlImpl
import com.alexrdclement.mediaplayground.media.engine.TimelineControl
import com.alexrdclement.mediaplayground.media.engine.TimelineControlImpl
import com.alexrdclement.mediaplayground.media.engine.PlaylistControl
import com.alexrdclement.mediaplayground.media.engine.PlaylistControlImpl
import com.alexrdclement.mediaplayground.media.engine.PlaylistState
import com.alexrdclement.mediaplayground.media.engine.PlaylistStateImpl
import com.alexrdclement.mediaplayground.media.engine.TransportControl
import com.alexrdclement.mediaplayground.media.engine.TransportControlImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaEngineBindingModule {
    @Binds
    val AndroidMediaEngineControl.bind: MediaEngineControl

    @Binds
    val AndroidMediaEngineState.bind: MediaEngineState

    @Binds
    val TransportControlImpl.bind: TransportControl

    @Binds
    val PlaylistControlImpl.bind: PlaylistControl

    @Binds
    val PlaylistStateImpl.bind: PlaylistState

    @Binds
    val PlayheadControlImpl.bind: PlayheadControl

    @Binds
    val TimelineControlImpl.bind: TimelineControl
}
