package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeArtist
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeSimpleAlbum
import com.alexrdclement.mediaplayground.media.mediaimport.factory.makeTrack
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toMediaImportError
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.metadata.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.SimpleAlbum
import com.alexrdclement.mediaplayground.media.model.Source
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.store.AlbumMediaStore
import com.alexrdclement.mediaplayground.media.store.ArtistMediaStore
import com.alexrdclement.mediaplayground.media.store.ClipMediaStore
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.media.store.ImageMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.PathProvider
import com.alexrdclement.mediaplayground.media.store.TrackMediaStore
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.mapFailure
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import java.util.UUID

class TrackImporterImpl @Inject constructor(
    private val transactionRunner: MediaStoreTransactionRunner,
    private val albumMediaStore: AlbumMediaStore,
    private val artistMediaStore: ArtistMediaStore,
    private val clipDataStore: ClipMediaStore,
    private val imageMediaStore: ImageMediaStore,
    private val mediaAssetStore: MediaAssetStore,
    private val trackMediaStore: TrackMediaStore,
    private val mediaMetadataRetriever: MediaMetadataRetriever,
    private val fileWriter: FileWriter,
    private val pathProvider: PathProvider,
) : TrackImporter {

    private data class ImportData(
        val uri: Uri,
        val mediaMetadata: MediaMetadata.Audio,
        val simpleAlbum: SimpleAlbum,
    )

    override suspend fun import(
        uri: Uri,
    ): Result<Track, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            val importData = getImportData(
                uri = uri,
                getArtistByName = artistMediaStore::getArtistByName,
                getAlbumByTitleAndArtistId = albumMediaStore::getAlbumByTitleAndArtistId,
            )
            import(
                importData = importData,
                saveTrack = ::save,
            )
        } catch (e: Throwable) {
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    override suspend fun import(
        uris: List<Uri>,
    ): Map<Uri, Result<Track, MediaImportError>> {
        // Album directory is only correct after a track has been saved. Import sequentially.
        return uris.associateWith { uri ->
            val importData = getImportData(
                uri = uri,
                getArtistByName = artistMediaStore::getArtistByName,
                getAlbumByTitleAndArtistId = albumMediaStore::getAlbumByTitleAndArtistId,
            )
            import(
                importData = importData,
                saveTrack = ::save,
            )
        }
    }

    private suspend fun getImportData(
        uri: Uri,
        getArtistByName: suspend (String) -> Artist?,
        getAlbumByTitleAndArtistId: suspend (String, ArtistId) -> SimpleAlbum?,
    ): ImportData {
        val mediaMetadata = mediaMetadataRetriever.getMediaMetadata(contentUri = uri) as MediaMetadata.Audio
        val artist = makeArtist(mediaMetadata, getArtistByName)
        val simpleAlbum = makeSimpleAlbum(
            mediaMetadata = mediaMetadata,
            artist = artist,
            getImageFilePath = pathProvider::getImagePath,
            getAlbumByTitleAndArtistId = getAlbumByTitleAndArtistId,
            source = Source.Local,
        )
        return ImportData(
            uri = uri,
            mediaMetadata = mediaMetadata,
            simpleAlbum = simpleAlbum
        )
    }

    private suspend fun import(
        importData: ImportData,
        saveTrack: suspend (Track) -> Unit,
    ): Result<Track, MediaImportError> {
        val imagePath = importData.simpleAlbum.images.firstOrNull()?.uri?.let { Path(it) }
        val fileWriteResult = writeToDisk(
            uri = importData.uri,
            embeddedPicture = importData.mediaMetadata.embeddedPicture,
            imagePath = imagePath,
            importDir = pathProvider.getAlbumDir(importData.simpleAlbum.id),
        )
        return when (fileWriteResult) {
            is Result.Failure -> Result.Failure(fileWriteResult.failure)
            is Result.Success -> {
                val track = makeTrack(
                    id = UUID.randomUUID(),
                    filePath = fileWriteResult.value,
                    mediaMetadata = importData.mediaMetadata,
                    artists = importData.simpleAlbum.artists,
                    simpleAlbum = importData.simpleAlbum,
                    source = Source.Local,
                )
                saveTrack(track)
                Result.Success(track)
            }
        }
    }

    private suspend fun writeToDisk(
        uri: Uri,
        embeddedPicture: ByteArray?,
        imagePath: Path?,
        importDir: Path,
    ): Result<Path, MediaImportError> = withContext(Dispatchers.IO) {
        try {
            try {
                SystemFileSystem.createDirectories(importDir)
            } catch (e: IOException) {
                return@withContext Result.Failure(MediaImportError.MkdirError)
            }

            if (embeddedPicture != null && imagePath != null) {
                try {
                    imagePath.parent?.let { SystemFileSystem.createDirectories(it) }
                } catch (e: IOException) {
                    // Fail silently — image write is best-effort
                }
                when (fileWriter.writeBitmapToDisk(embeddedPicture, imagePath)) {
                    is Result.Failure -> Unit // Fail silently for now
                    is Result.Success -> Unit
                }
            }

            return@withContext fileWriter.writeToDisk(
                contentUri = uri,
                destinationDir = importDir,
            ).mapFailure { it.toMediaImportError() }
        } catch (e: Throwable) {
            Result.Failure(MediaImportError.Unknown(throwable = e))
        }
    }

    private suspend fun save(track: Track) {
        transactionRunner.run {
            for (artist in track.artists) {
                artistMediaStore.put(artist)
            }

            for (trackClip in track.clips) {
                mediaAssetStore.put(trackClip.clip.mediaAsset)
                clipDataStore.put(trackClip.clip)
            }

            imageMediaStore.put(track.images.toSet())

            albumMediaStore.put(track.simpleAlbum)

            trackMediaStore.put(track)
        }
    }
}
