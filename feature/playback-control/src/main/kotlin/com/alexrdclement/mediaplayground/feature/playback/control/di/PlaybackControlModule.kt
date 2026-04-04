package com.alexrdclement.mediaplayground.feature.playback.control.di

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.feature.playback.control.PlaybackControlViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@BindingContainer
interface PlaybackControlModule {
    companion object {
        @Provides
        @IntoMap
        @ViewModelKey(PlaybackControlViewModel::class)
        fun providePlaybackControlViewModel(viewModel: PlaybackControlViewModel): ViewModel = viewModel
    }
}
