package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.mediaplayground.data.album.di.AlbumBindingModule
import com.alexrdclement.mediaplayground.data.album.local.di.LocalAlbumBindingModule
import com.alexrdclement.mediaplayground.data.artist.di.ArtistBindingModule
import com.alexrdclement.mediaplayground.data.artist.local.di.LocalArtistBindingModule
import com.alexrdclement.mediaplayground.data.disk.di.PathProviderModule
import com.alexrdclement.mediaplayground.data.image.di.ImageBindingModule
import com.alexrdclement.mediaplayground.data.image.local.di.LocalImageBindingModule
import com.alexrdclement.mediaplayground.data.track.di.TrackBindingModule
import com.alexrdclement.mediaplayground.data.track.local.di.LocalTrackBindingModule
import com.alexrdclement.mediaplayground.database.di.DaoModule
import com.alexrdclement.mediaplayground.database.di.DatabaseModule
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface DataModule :
    AlbumBindingModule,
    LocalAlbumBindingModule,
    ArtistBindingModule,
    LocalArtistBindingModule,
    ImageBindingModule,
    LocalImageBindingModule,
    TrackBindingModule,
    LocalTrackBindingModule,
    PathProviderModule,
    DatabaseModule,
    DaoModule
