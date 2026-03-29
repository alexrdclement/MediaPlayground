package com.alexrdclement.mediaplayground.data.artist.local.di

import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistRepository
import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface LocalArtistBindingModule {
    @Binds val LocalArtistRepositoryImpl.bind: LocalArtistRepository
}
