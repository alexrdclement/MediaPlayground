package com.alexrdclement.mediaplayground.data.album.di

import com.alexrdclement.mediaplayground.data.album.AlbumMediaStoreImpl
import com.alexrdclement.mediaplayground.media.store.AlbumMediaStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface AlbumMediaStoreModule {
    @Binds val AlbumMediaStoreImpl.bindMediaStore: AlbumMediaStore
}
