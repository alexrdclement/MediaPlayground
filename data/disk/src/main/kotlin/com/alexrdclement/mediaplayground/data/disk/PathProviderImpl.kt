package com.alexrdclement.mediaplayground.data.disk

import android.app.Application
import android.os.Environment
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
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

    override fun getAlbumDir(albumId: AlbumId): Path {
        return Path(audioImportDir, albumId.value)
    }

    override fun getImagesDir(): Path {
        return Path(imageImportDir, "images")
    }

    override fun getImagePath(imageId: ImageId, extension: String): Path {
        return Path(imageImportDir, "images", "${imageId.value}.$extension")
    }
}
