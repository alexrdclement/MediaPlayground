package com.alexrdclement.mediaplayground.data.artist.di

import com.alexrdclement.mediaplayground.data.artist.ArtistRepository
import com.alexrdclement.mediaplayground.data.artist.ArtistRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface ArtistModule {
    @Binds val ArtistRepositoryImpl.bind: ArtistRepository
}
