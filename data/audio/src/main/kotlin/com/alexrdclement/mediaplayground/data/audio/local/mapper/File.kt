package com.alexrdclement.mediaplayground.data.audio.local.mapper

import kotlinx.io.files.Path

internal fun uriFromFileName(mediaItemDir: Path, fileName: String?): String? {
    return fileName?.let { Path(mediaItemDir, it) }?.toString()
}

internal fun fileNameFromUri(uri: String?): String? {
    return uri?.let { Path(it).name }
}
