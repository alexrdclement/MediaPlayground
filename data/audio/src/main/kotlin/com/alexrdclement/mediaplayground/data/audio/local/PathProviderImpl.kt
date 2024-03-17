package com.alexrdclement.mediaplayground.data.audio.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.io.files.Path
import javax.inject.Inject

class PathProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : PathProvider {
    override val trackImportFileWriteDir: Path = Path(context.cacheDir.absolutePath)
}
