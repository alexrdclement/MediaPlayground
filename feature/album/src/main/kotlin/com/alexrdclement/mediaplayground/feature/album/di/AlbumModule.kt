package com.alexrdclement.mediaplayground.feature.album.di

import com.alexrdclement.mediaplayground.feature.album.AlbumViewModel
import com.alexrdclement.mediaplayground.feature.album.delete.AlbumDeleteViewModel
import com.alexrdclement.mediaplayground.feature.album.metadata.AlbumMetadataViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey

@BindingContainer
interface AlbumModule {
    companion object {
        @Provides
        @IntoMap
        @ManualViewModelAssistedFactoryKey(AlbumViewModel.Factory::class)
        fun provideAlbumViewModelFactory(
            factory: AlbumViewModel.Factory,
        ): ManualViewModelAssistedFactory = factory

        @Provides
        @IntoMap
        @ManualViewModelAssistedFactoryKey(AlbumMetadataViewModel.Factory::class)
        fun provideAlbumMetadataViewModelFactory(
            factory: AlbumMetadataViewModel.Factory,
        ): ManualViewModelAssistedFactory = factory

        @Provides
        @IntoMap
        @ManualViewModelAssistedFactoryKey(AlbumDeleteViewModel.Factory::class)
        fun provideAlbumDeleteViewModelFactory(
            factory: AlbumDeleteViewModel.Factory,
        ): ManualViewModelAssistedFactory = factory
    }
}
