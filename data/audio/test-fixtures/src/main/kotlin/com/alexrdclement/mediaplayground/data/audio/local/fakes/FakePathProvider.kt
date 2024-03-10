package com.alexrdclement.mediaplayground.data.audio.local.fakes

import com.alexrdclement.mediaplayground.data.audio.local.PathProvider
import kotlinx.io.files.Path
import javax.inject.Inject

class FakePathProvider @Inject constructor(): PathProvider {
    override val trackImportFileWriteDir: Path = Path("file://")
}
