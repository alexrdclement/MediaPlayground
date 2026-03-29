package com.alexrdclement.mediaplayground.feature.track.di

import com.alexrdclement.mediaplayground.feature.track.TrackMetadataViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey

@BindingContainer
interface TrackModule {
    companion object {
        @Provides
        @IntoMap
        @ManualViewModelAssistedFactoryKey(TrackMetadataViewModel.Factory::class)
        fun provideTrackMetadataViewModelFactory(
            factory: TrackMetadataViewModel.Factory,
        ): ManualViewModelAssistedFactory = factory
    }
}
