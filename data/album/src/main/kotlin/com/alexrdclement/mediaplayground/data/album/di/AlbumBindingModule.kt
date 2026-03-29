package com.alexrdclement.mediaplayground.data.album.di

import com.alexrdclement.mediaplayground.data.album.AlbumRepository
import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumRepository
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface AlbumBindingModule {
    @Binds val LocalAlbumRepository.bind: AlbumRepository
}
