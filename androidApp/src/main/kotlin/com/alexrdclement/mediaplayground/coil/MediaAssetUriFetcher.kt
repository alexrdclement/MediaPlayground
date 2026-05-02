package com.alexrdclement.mediaplayground.coil

import coil3.ImageLoader
import coil3.decode.DataSource
import coil3.decode.ImageSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.SourceFetchResult
import coil3.request.Options
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.store.PathProvider
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import java.io.File
import java.io.FileNotFoundException

class MediaAssetUriFetcher(
    private val uri: MediaAssetUri,
    private val pathProvider: PathProvider,
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val path = pathProvider.getPath(uri)
        val file = File(path.toString())
        if (!file.exists()) throw FileNotFoundException(file.toString())
        return SourceFetchResult(
            source = ImageSource(file = file.toOkioPath(), fileSystem = FileSystem.SYSTEM),
            mimeType = null,
            dataSource = DataSource.DISK,
        )
    }

    class Factory(private val pathProvider: PathProvider) : Fetcher.Factory<MediaAssetUri> {
        override fun create(
            data: MediaAssetUri,
            options: Options,
            imageLoader: ImageLoader,
        ): Fetcher = MediaAssetUriFetcher(data, pathProvider)
    }
}
