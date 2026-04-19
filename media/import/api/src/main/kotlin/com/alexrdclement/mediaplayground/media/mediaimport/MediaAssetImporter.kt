package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

interface MediaAssetImporter {
    suspend fun import(
        uri: Uri,
    ): Result<MediaAssetImportResult, MediaImportError>
}

sealed interface MediaAssetImportResult {
    data class Audio(
        val filePath: Path,
        val audioAsset: AudioAsset,
        val simpleAlbum: SimpleAlbum
    ) : MediaAssetImportResult

    data class Image(
        val path: Path,
        val image: com.alexrdclement.mediaplayground.media.model.Image,
    ) : MediaAssetImportResult
}
