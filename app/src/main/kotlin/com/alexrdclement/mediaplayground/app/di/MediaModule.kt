package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.mediaplayground.media.engine.di.MediaEngineBindingModule
import com.alexrdclement.mediaplayground.media.mediaimport.di.MediaImportModule
import com.alexrdclement.mediaplayground.media.metadata.di.MediaMetadataRetrieverModule
import com.alexrdclement.mediaplayground.media.session.di.MediaSessionBindingModule
import com.alexrdclement.mediaplayground.media.store.di.MediaStoreModule
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface MediaModule :
    MediaEngineBindingModule,
    MediaImportModule,
    MediaMetadataRetrieverModule,
    MediaSessionBindingModule,
    MediaStoreModule
