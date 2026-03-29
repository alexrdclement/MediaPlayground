package com.alexrdclement.mediaplayground.data.album.local.di

import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumRepository
import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface LocalAlbumBindingModule {
    @Binds val LocalAlbumRepositoryImpl.bind: LocalAlbumRepository
}
