package com.alexrdclement.mediaplayground.media.store.di

import com.alexrdclement.mediaplayground.media.store.FileReader
import com.alexrdclement.mediaplayground.media.store.FileReaderImpl
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.media.store.FileWriterImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaStoreModule {
    @Binds
    val FileReaderImpl.bind: FileReader

    @Binds
    val FileWriterImpl.bind: FileWriter
}
