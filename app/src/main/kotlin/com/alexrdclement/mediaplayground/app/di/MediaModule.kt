package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.mediaplayground.media.engine.di.MediaEngineBindingModule
import com.alexrdclement.mediaplayground.media.mediaimport.di.FileWriterModule
import com.alexrdclement.mediaplayground.media.mediaimport.di.MediaMetadataRetrieverModule
import com.alexrdclement.mediaplayground.media.session.di.MediaSessionBindingModule
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface MediaModule :
    MediaEngineBindingModule,
    FileWriterModule,
    MediaMetadataRetrieverModule,
    MediaSessionBindingModule
