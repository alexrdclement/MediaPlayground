package com.alexrdclement.mediaplayground.app.di

import com.alexrdclement.mediaplayground.media.store.PathProvider
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

interface MetroApp {
    val appGraph: AppGraph
    val viewModelFactory: MetroViewModelFactory get() = appGraph.metroViewModelFactory
    val pathProvider: PathProvider get() = appGraph.pathProvider
}
