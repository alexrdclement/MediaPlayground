package com.alexrdclement.mediaplayground.app.di

import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

interface MetroApp {
    val appGraph: AppGraph
    val viewModelFactory: MetroViewModelFactory get() = appGraph.metroViewModelFactory
}
