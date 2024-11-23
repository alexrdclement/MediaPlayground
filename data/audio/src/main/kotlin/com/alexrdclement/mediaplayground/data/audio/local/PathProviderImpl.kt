package com.alexrdclement.mediaplayground.data.audio.local

import android.content.Context
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.io.files.Path
import javax.inject.Inject

class PathProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : PathProvider {
    override val trackImportFileWriteDir: Path
        get() {
            val defaultPath = Path(context.filesDir.absolutePath)

            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                return defaultPath
            }

            val externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
            if (externalFilesDir != null) {
                return Path(externalFilesDir.absolutePath)
            }

            return defaultPath
        }
}
