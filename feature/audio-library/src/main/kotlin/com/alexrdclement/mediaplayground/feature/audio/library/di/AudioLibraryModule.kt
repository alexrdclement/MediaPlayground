package com.alexrdclement.mediaplayground.feature.audio.library.di

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@BindingContainer
interface AudioLibraryModule {
    companion object {
        @Provides
        @IntoMap
        @ViewModelKey(AudioLibraryViewModel::class)
        fun provideAudioLibraryViewModel(viewModel: AudioLibraryViewModel): ViewModel = viewModel
    }
}
