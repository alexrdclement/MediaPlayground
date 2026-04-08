package com.alexrdclement.mediaplayground.media.mediaimport.di

import com.alexrdclement.mediaplayground.media.mediaimport.AlbumImporter
import com.alexrdclement.mediaplayground.media.mediaimport.AlbumImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.ArtistImporter
import com.alexrdclement.mediaplayground.media.mediaimport.ArtistImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.AudioFileImporter
import com.alexrdclement.mediaplayground.media.mediaimport.AudioFileImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.ClipImporter
import com.alexrdclement.mediaplayground.media.mediaimport.ClipImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.ImageImporter
import com.alexrdclement.mediaplayground.media.mediaimport.ImageImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.TrackImporter
import com.alexrdclement.mediaplayground.media.mediaimport.TrackImporterImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaImportModule {
    @Binds
    val AlbumImporterImpl.bind: AlbumImporter

    @Binds
    val ArtistImporterImpl.bind: ArtistImporter

    @Binds
    val AudioFileImporterImpl.bind: AudioFileImporter

    @Binds
    val ClipImporterImpl.bind: ClipImporter

    @Binds
    val ImageImporterImpl.bind: ImageImporter

    @Binds
    val TrackImporterImpl.bind: TrackImporter
}
