package com.alexrdclement.mediaplayground.app.di

import androidx.lifecycle.ViewModel
import com.alexrdclement.logging.Logger
import com.alexrdclement.logging.LoggerImpl
import com.alexrdclement.mediaplayground.app.AppViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.IntoMap
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@ContributesTo(AppScope::class)
@BindingContainer
interface AppModule {
    companion object {
        @Provides
        @SingleIn(AppScope::class)
        fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        @Provides
        @SingleIn(AppScope::class)
        fun provideLogger(
            coroutineScope: CoroutineScope,
        ): Logger = LoggerImpl(
            coroutineScope = coroutineScope,
        )

        @Provides
        @IntoMap
        @ViewModelKey(AppViewModel::class)
        fun provideAppViewModel(viewModel: AppViewModel): ViewModel = viewModel
    }
}
