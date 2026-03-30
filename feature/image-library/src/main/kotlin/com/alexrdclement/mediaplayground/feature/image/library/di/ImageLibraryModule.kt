package com.alexrdclement.mediaplayground.feature.image.library.di

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.feature.image.library.ImageLibraryViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@BindingContainer
interface ImageLibraryModule {
    companion object {
        @Provides
        @IntoMap
        @ViewModelKey(ImageLibraryViewModel::class)
        fun provideImageLibraryViewModel(viewModel: ImageLibraryViewModel): ViewModel = viewModel
    }
}
