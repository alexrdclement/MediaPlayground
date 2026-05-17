package com.alexrdclement.mediaplayground.data.mediacollection.di

import com.alexrdclement.mediaplayground.data.mediacollection.MediaCollectionRepository
import com.alexrdclement.mediaplayground.data.mediacollection.MediaCollectionRepositoryImpl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds

@BindingContainer
interface MediaCollectionModule {
    @Binds val MediaCollectionRepositoryImpl.bind: MediaCollectionRepository
}
