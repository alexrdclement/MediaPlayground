package com.alexrdclement.mediaplayground.media.mediaimport.di

import com.alexrdclement.mediaplayground.media.mediaimport.ImageImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.ImageImporter
import com.alexrdclement.mediaplayground.media.mediaimport.TrackImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.TrackImporter
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaImportModule {
    @Binds
    val ImageImporterImpl.bind: ImageImporter

    @Binds
    val TrackImporterImpl.bindTrackImporter: TrackImporter
}
