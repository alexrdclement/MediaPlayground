package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

interface MediaAssetImporter {
    suspend fun import(
        uri: Uri,
        source: Source,
    ): Result<MediaAssetImportResult, MediaImportError>
}

sealed interface MediaAssetImportResult {
    data class Audio(
        val filePath: Path,
        val mediaAsset: MediaAsset,
        val simpleAlbum: SimpleAlbum
    ) : MediaAssetImportResult

    data class Image(
        val path: Path,
        val image: com.alexrdclement.mediaplayground.media.model.Image,
    ) : MediaAssetImportResult
}
