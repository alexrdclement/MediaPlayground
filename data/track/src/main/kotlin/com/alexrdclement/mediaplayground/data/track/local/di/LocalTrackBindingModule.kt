package com.alexrdclement.mediaplayground.data.track.local.di

import com.alexrdclement.mediaplayground.data.track.local.LocalTrackRepository
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface LocalTrackBindingModule {
    @Binds val LocalTrackRepositoryImpl.bind: LocalTrackRepository
}
