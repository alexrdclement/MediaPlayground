package com.alexrdclement.mediaplayground.data.audio.local

import android.app.Application
import android.os.Environment
import dev.zacsweers.metro.Inject
import kotlinx.io.files.Path

class PathProviderImpl @Inject constructor(
    private val application: Application,
) : PathProvider {
    private val audioImportDir: Path
        get() {
            val defaultPath = Path(application.filesDir.absolutePath)

            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                return defaultPath
            }

            val externalFilesDir = application.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            if (externalFilesDir != null) {
                return Path(externalFilesDir.absolutePath)
            }

            return defaultPath
        }

    override fun getAlbumDir(albumId: String): Path {
        return Path(audioImportDir, albumId)
    }
}
