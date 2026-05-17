package com.alexrdclement.mediaplayground.data.clip.di

import com.alexrdclement.mediaplayground.data.clip.ClipMediaStoreImpl
import com.alexrdclement.mediaplayground.media.store.ClipMediaStore
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface ClipMediaStoreModule {
    @Binds val ClipMediaStoreImpl.bind: ClipMediaStore
}
