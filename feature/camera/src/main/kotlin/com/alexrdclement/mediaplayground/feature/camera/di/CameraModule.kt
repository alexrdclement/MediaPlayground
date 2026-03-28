package com.alexrdclement.mediaplayground.feature.camera.di

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.feature.camera.CameraViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@BindingContainer
interface CameraModule {
    companion object {
        @Provides
        @IntoMap
        @ViewModelKey(CameraViewModel::class)
        fun provideCameraViewModel(viewModel: CameraViewModel): ViewModel = viewModel
    }
}
