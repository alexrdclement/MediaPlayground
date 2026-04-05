package com.alexrdclement.mediaplayground.media.mediaimport.di

import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaImportModule {
    @Binds
    val MediaImporterImpl.bind: MediaImporter
}
