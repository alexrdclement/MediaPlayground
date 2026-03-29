package com.alexrdclement.mediaplayground.feature.image.di

import com.alexrdclement.mediaplayground.feature.image.ImageMetadataViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey

@BindingContainer
interface ImageModule {
    companion object {
        @Provides
        @IntoMap
        @ManualViewModelAssistedFactoryKey(ImageMetadataViewModel.Factory::class)
        fun provideImageMetadataViewModelFactory(
            factory: ImageMetadataViewModel.Factory,
        ): ManualViewModelAssistedFactory = factory
    }
}
