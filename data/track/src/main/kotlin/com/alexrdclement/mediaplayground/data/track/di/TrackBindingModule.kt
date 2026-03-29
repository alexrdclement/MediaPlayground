package com.alexrdclement.mediaplayground.data.track.di

import com.alexrdclement.mediaplayground.data.track.TrackRepository
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackRepository
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface TrackBindingModule {
    @Binds val LocalTrackRepository.bind: TrackRepository
}
