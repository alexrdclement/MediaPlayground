package com.alexrdclement.mediaplayground.data.album.di

import com.alexrdclement.mediaplayground.data.album.AlbumRepository
import com.alexrdclement.mediaplayground.data.album.AlbumRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface AlbumModule {
    @Binds val AlbumRepositoryImpl.bindRepository: AlbumRepository
}
