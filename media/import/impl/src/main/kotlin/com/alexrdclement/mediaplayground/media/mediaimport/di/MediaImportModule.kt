package com.alexrdclement.mediaplayground.media.mediaimport.di

import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporterImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaImportModule {
    @Binds
    val MediaImporterImpl.bind: MediaImporter
}
