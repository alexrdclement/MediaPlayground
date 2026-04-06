package com.alexrdclement.mediaplayground.data.track.di

import com.alexrdclement.mediaplayground.data.track.TrackRepository
import com.alexrdclement.mediaplayground.data.track.TrackRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface TrackModule {
    @Binds val TrackRepositoryImpl.bind: TrackRepository
}
