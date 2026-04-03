package com.alexrdclement.mediaplayground.feature.artist.di

import com.alexrdclement.mediaplayground.feature.artist.ArtistMetadataViewModel
import com.alexrdclement.mediaplayground.feature.artist.delete.ArtistDeleteViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey

@BindingContainer
interface ArtistModule {
    companion object {
        @Provides
        @IntoMap
        @ManualViewModelAssistedFactoryKey(ArtistMetadataViewModel.Factory::class)
        fun provideArtistMetadataViewModelFactory(
            factory: ArtistMetadataViewModel.Factory,
        ): ManualViewModelAssistedFactory = factory

        @Provides
        @IntoMap
        @ManualViewModelAssistedFactoryKey(ArtistDeleteViewModel.Factory::class)
        fun provideArtistDeleteViewModelFactory(
            factory: ArtistDeleteViewModel.Factory,
        ): ManualViewModelAssistedFactory = factory
    }
}
