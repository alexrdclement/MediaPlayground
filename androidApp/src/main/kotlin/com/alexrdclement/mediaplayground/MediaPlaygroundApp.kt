package com.alexrdclement.mediaplayground

import android.app.Application
import com.alexrdclement.mediaplayground.app.di.AppGraph
import com.alexrdclement.mediaplayground.app.di.MetroApp
import dev.zacsweers.metro.createGraphFactory

class MediaPlaygroundApp : Application(), MetroApp {
    override val appGraph: AppGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }
}
