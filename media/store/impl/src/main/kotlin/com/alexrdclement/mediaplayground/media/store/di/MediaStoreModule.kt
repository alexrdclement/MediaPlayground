package com.alexrdclement.mediaplayground.media.store.di

import com.alexrdclement.mediaplayground.media.store.ContentUriReader
import com.alexrdclement.mediaplayground.media.store.ContentUriReaderImpl
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.media.store.FileWriterImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaStoreModule {
    @Binds
    val ContentUriReaderImpl.bind: ContentUriReader

    @Binds
    val FileWriterImpl.bind: FileWriter
}
