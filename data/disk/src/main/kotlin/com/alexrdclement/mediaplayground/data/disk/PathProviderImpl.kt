package com.alexrdclement.mediaplayground.data.disk

import android.app.Application
import android.os.Environment
import dev.zacsweers.metro.Inject
import kotlinx.io.files.Path

class PathProviderImpl @Inject constructor(
    private val application: Application,
) : PathProvider {
    private val audioImportDir: Path
        get() = externalFilesDir(Environment.DIRECTORY_MUSIC)

    private val imageImportDir: Path
        get() = externalFilesDir(Environment.DIRECTORY_PICTURES)

    private fun externalFilesDir(type: String): Path {
        val isExternalStorageMounted = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        val externalFilesDir = if (isExternalStorageMounted) application.getExternalFilesDir(type) else null
        val dir = externalFilesDir ?: application.filesDir
        return Path(dir.absolutePath)
    }

    override fun getAlbumDir(albumId: String): Path {
        return Path(audioImportDir, albumId)
    }

    override fun getImagesDir(): Path {
        return Path(imageImportDir, "images")
    }
}
