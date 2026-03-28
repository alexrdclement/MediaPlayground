package com.alexrdclement.mediaplayground.media.mediaimport.di

import com.alexrdclement.mediaplayground.media.mediaimport.FileWriter
import com.alexrdclement.mediaplayground.media.mediaimport.FileWriterImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface FileWriterModule {
    @Binds
    val FileWriterImpl.bind: FileWriter
}
