package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.mediaplayground.data.album.di.AlbumMediaStoreModule
import com.alexrdclement.mediaplayground.data.album.di.AlbumModule
import com.alexrdclement.mediaplayground.data.artist.di.ArtistMediaStoreModule
import com.alexrdclement.mediaplayground.data.artist.di.ArtistModule
import com.alexrdclement.mediaplayground.data.clip.di.ClipMediaStoreModule
import com.alexrdclement.mediaplayground.data.clip.di.ClipModule
import com.alexrdclement.mediaplayground.data.disk.di.PathProviderModule
import com.alexrdclement.mediaplayground.data.image.di.ImageMediaStoreModule
import com.alexrdclement.mediaplayground.data.image.di.ImageModule
import com.alexrdclement.mediaplayground.data.media.di.MediaBindingModule
import com.alexrdclement.mediaplayground.data.mediaasset.di.MediaAssetModule
import com.alexrdclement.mediaplayground.data.mediaasset.di.MediaAssetStoreModule
import com.alexrdclement.mediaplayground.data.track.di.TrackMediaStoreModule
import com.alexrdclement.mediaplayground.data.track.di.TrackModule
import com.alexrdclement.mediaplayground.database.di.DaoModule
import com.alexrdclement.mediaplayground.database.di.DatabaseModule
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface DataModule :
    AlbumModule,
    AlbumMediaStoreModule,
    ArtistModule,
    ArtistMediaStoreModule,
    ClipModule,
    ClipMediaStoreModule,
    DatabaseModule,
    DaoModule,
    ImageModule,
    ImageMediaStoreModule,
    MediaAssetModule,
    MediaAssetStoreModule,
    MediaBindingModule,
    TrackModule,
    TrackMediaStoreModule,
    PathProviderModule
