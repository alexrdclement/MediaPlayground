package com.alexrdclement.mediaplayground.media.session.di

import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionControlImpl
import com.alexrdclement.mediaplayground.media.session.MediaSessionEntry
import com.alexrdclement.mediaplayground.media.session.MediaSessionEntryImpl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.MediaSessionStateImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaSessionBindingModule {
    @Binds
    val MediaSessionEntryImpl.bind: MediaSessionEntry

    @Binds
    val MediaSessionControlImpl.bind: MediaSessionControl

    @Binds
    val MediaSessionStateImpl.bind: MediaSessionState
}
