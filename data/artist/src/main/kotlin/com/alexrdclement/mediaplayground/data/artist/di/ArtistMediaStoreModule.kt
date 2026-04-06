package com.alexrdclement.mediaplayground.data.artist.di

import com.alexrdclement.mediaplayground.data.artist.ArtistMediaStoreImpl
import com.alexrdclement.mediaplayground.media.store.ArtistMediaStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface ArtistMediaStoreModule {
    @Binds val ArtistMediaStoreImpl.bind: ArtistMediaStore
}
