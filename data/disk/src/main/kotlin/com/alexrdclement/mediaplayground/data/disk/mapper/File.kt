package com.alexrdclement.mediaplayground.data.disk.mapper

import kotlinx.io.files.Path

fun uriFromFileName(mediaItemDir: Path, fileName: String?): String? {
    return fileName?.let { Path(mediaItemDir, it) }?.toString()
}

fun fileNameFromUri(uri: String?): String? {
    return uri?.let { Path(it).name }
}
