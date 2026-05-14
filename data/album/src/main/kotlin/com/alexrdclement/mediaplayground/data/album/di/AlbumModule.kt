package com.alexrdclement.mediaplayground.data.album.di

import com.alexrdclement.mediaplayground.data.album.AudioAlbumRepository
import com.alexrdclement.mediaplayground.data.album.AudioAlbumRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface AlbumModule {
    @Binds val AudioAlbumRepositoryImpl.bindRepository: AudioAlbumRepository
}
