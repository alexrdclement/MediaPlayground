package com.alexrdclement.mediaplayground.app.di

import android.app.Application
import com.alexrdclement.mediaplayground.media.store.PathProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

@DependencyGraph(scope = AppScope::class)
interface AppGraph : ViewModelGraph {

    val pathProvider: PathProvider

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides application: Application): AppGraph
    }
}
