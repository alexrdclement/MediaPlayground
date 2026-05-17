package com.alexrdclement.mediaplayground.data.track.di

import com.alexrdclement.mediaplayground.data.track.TrackMediaStoreImpl
import com.alexrdclement.mediaplayground.media.store.TrackMediaStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface TrackMediaStoreModule {
    @Binds val TrackMediaStoreImpl.bind: TrackMediaStore
}
