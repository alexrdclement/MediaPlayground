package com.alexrdclement.mediaplayground

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.alexrdclement.mediaplayground.app.di.AppGraph
import com.alexrdclement.mediaplayground.app.di.MetroApp
import com.alexrdclement.mediaplayground.media.ui.coil.MediaAssetUriFetcher
import com.alexrdclement.mediaplayground.media.ui.coil.MediaAssetUriMapper
import dev.zacsweers.metro.createGraphFactory

class MediaPlaygroundApp : Application(), MetroApp, SingletonImageLoader.Factory {
    override val appGraph: AppGraph by lazy { createGraphFactory<AppGraph.Factory>().create(this) }

    override fun newImageLoader(context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .components {
                add(MediaAssetUriMapper())
                add(MediaAssetUriFetcher.Factory(pathProvider))
            }
            .build()
}
