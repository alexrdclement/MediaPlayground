package com.alexrdclement.mediaplayground.data.artist.di

import com.alexrdclement.mediaplayground.data.artist.ArtistRepository
import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistRepository
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface ArtistBindingModule {
    @Binds val LocalArtistRepository.bind: ArtistRepository
}
