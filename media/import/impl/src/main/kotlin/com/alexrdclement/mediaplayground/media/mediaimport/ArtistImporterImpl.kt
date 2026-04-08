package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.model.mapper.toArtist
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.store.ArtistMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.model.result.Result
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

@Inject
class ArtistImporterImpl(
    private val artistMediaStore: ArtistMediaStore,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val transactionRunner: MediaStoreTransactionRunner,
) : ArtistImporter {

    override suspend fun import(uri: Uri): Result<Artist, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val metadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as? MediaMetadata.Audio
                ?: return@withContext Result.Failure(MediaImportError.InputFileError)
            transactionRunner.run { importTransaction(metadata) }
        } catch (e: Throwable) {
            ensureActive()
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    override suspend fun import(uris: List<Uri>): Map<Uri, Result<Artist, MediaImportError>> =
        uris.associateWith { import(it) }

    context(scope: MediaStoreTransactionScope)
    internal suspend fun importTransaction(
        metadata: MediaMetadata.Audio,
    ): Result<Artist, MediaImportError> {
        val artistName = metadata.artistName ?: "Unknown artist"
        val existing = artistMediaStore.getArtistByName(artistName)
        if (existing != null) return Result.Success(existing)
        val artist = metadata.toArtist(artistName)
        artistMediaStore.put(artist)
        return Result.Success(artist)
    }
}
