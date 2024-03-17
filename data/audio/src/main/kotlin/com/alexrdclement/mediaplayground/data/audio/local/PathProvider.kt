package com.alexrdclement.mediaplayground.data.audio.local

import kotlinx.io.files.Path

interface PathProvider {
    val trackImportFileWriteDir: Path
}
