package com.alexrdclement.mediaplayground.feature.player.di

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.feature.player.PlayerViewModel
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@BindingContainer
interface PlayerModule {
    companion object {
        @Provides
        @IntoMap
        @ViewModelKey(PlayerViewModel::class)
        fun providePlayerViewModel(viewModel: PlayerViewModel): ViewModel = viewModel
    }
}
