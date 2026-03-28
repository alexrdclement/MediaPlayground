package com.alexrdclement.mediaplayground.feature.media.control.di

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.feature.media.control.MediaControlSheetViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@BindingContainer
interface MediaControlModule {
    companion object {
        @Provides
        @IntoMap
        @ViewModelKey(MediaControlSheetViewModel::class)
        fun provideMediaControlSheetViewModel(viewModel: MediaControlSheetViewModel): ViewModel = viewModel
    }
}
