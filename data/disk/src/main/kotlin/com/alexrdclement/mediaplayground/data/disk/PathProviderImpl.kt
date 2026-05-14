package com.alexrdclement.mediaplayground.data.disk

import android.app.Application
import android.os.Environment
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import dev.zacsweers.metro.Inject
import kotlinx.io.files.Path

class PathProviderImpl @Inject constructor(
    private val application: Application,
) : PathProvider {
    private val audioImportDir: Path
        get() = externalFilesDir(Environment.DIRECTORY_MUSIC)

    private val imageImportDir: Path
        get() = externalFilesDir(Environment.DIRECTORY_PICTURES)

    override fun getPath(uri: MediaAssetUri): Path = when (uri) {
        is MediaAssetUri.Shared -> Path(imageImportDir, "images", uri.fileName)
        is MediaAssetUri.Album -> Path(audioImportDir, uri.albumId.value, uri.fileName)
    }

    private fun externalFilesDir(type: String): Path {
        val defaultPath = Path(application.filesDir.absolutePath)

        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return defaultPath
        }

        val externalFilesDir = application.getExternalFilesDir(type)
        if (externalFilesDir != null) {
            return Path(externalFilesDir.absolutePath)
        }

        return defaultPath
    }
}
