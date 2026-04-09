package com.alexrdclement.media.mediaimport.fixtures

import com.alexrdclement.media.metadata.FakeMediaMetadataRetriever
import com.alexrdclement.media.store.FakeAlbumMediaStore
import com.alexrdclement.media.store.FakeArtistMediaStore
import com.alexrdclement.media.store.FakeClipMediaStore
import com.alexrdclement.media.store.FakeFileReader
import com.alexrdclement.media.store.FakeFileWriter
import com.alexrdclement.media.store.FakeImageMediaStore
import com.alexrdclement.media.store.FakeMediaAssetStore
import com.alexrdclement.media.store.FakeMediaStoreTransactionRunner
import com.alexrdclement.media.store.FakePathProvider
import com.alexrdclement.media.store.FakeTrackMediaStore
import com.alexrdclement.mediaplayground.media.mediaimport.AlbumImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.ArtistImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.AudioFileImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.ClipImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.ImageImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.MediaAssetImporterImpl
import com.alexrdclement.mediaplayground.media.mediaimport.TrackImporterImpl

class MediaImporterFixture(
    val mediaMetadataRetriever: FakeMediaMetadataRetriever = FakeMediaMetadataRetriever(),
    val fileReader: FakeFileReader = FakeFileReader(),
    val fileWriter: FakeFileWriter = FakeFileWriter(),
) {
    val artistMediaStore = FakeArtistMediaStore()
    val albumMediaStore = FakeAlbumMediaStore()
    val clipMediaStore = FakeClipMediaStore()
    val trackMediaStore = FakeTrackMediaStore()
    val imageMediaStore = FakeImageMediaStore()
    val mediaAssetStore = FakeMediaAssetStore()
    val transactionRunner = FakeMediaStoreTransactionRunner()
    val pathProvider = FakePathProvider()

    val artistImporter = ArtistImporterImpl(
        artistMediaStore = artistMediaStore,
        mediaMetadataRetriever = mediaMetadataRetriever,
        transactionRunner = transactionRunner,
    )

    val imageImporter = ImageImporterImpl(
        mediaMetadataRetriever = mediaMetadataRetriever,
        pathProvider = pathProvider,
        imageMediaStore = imageMediaStore,
        transactionRunner = transactionRunner,
        fileReader = fileReader,
        fileWriter = fileWriter,
    )

    val albumImporter: AlbumImporterImpl by lazy {
        AlbumImporterImpl(
            albumMediaStore = albumMediaStore,
            mediaAssetImporter = lazy { mediaAssetImporter },
            transactionRunner = transactionRunner,
            mediaMetadataRetriever = mediaMetadataRetriever,
            trackImporter = lazy { trackImporter },
        )
    }

    val clipImporter: ClipImporterImpl by lazy {
        ClipImporterImpl(
            clipDataStore = clipMediaStore,
            mediaAssetImporter = lazy { mediaAssetImporter },
            mediaMetadataRetriever = mediaMetadataRetriever,
            transactionRunner = transactionRunner,
        )
    }

    val audioFileImporter: AudioFileImporterImpl by lazy {
        AudioFileImporterImpl(
            mediaAssetStore = mediaAssetStore,
            mediaMetadataRetriever = mediaMetadataRetriever,
            artistImporter = artistImporter,
            albumImporter = albumImporter,
            pathProvider = pathProvider,
            fileWriter = fileWriter,
            imageImporter = imageImporter,
            transactionRunner = transactionRunner,
        )
    }

    val trackImporter: TrackImporterImpl by lazy {
        TrackImporterImpl(
            trackMediaStore = trackMediaStore,
            mediaAssetImporter = lazy { mediaAssetImporter },
            clipImporter = lazy { clipImporter },
            mediaMetadataRetriever = mediaMetadataRetriever,
            transactionRunner = transactionRunner,
        )
    }

    val mediaAssetImporter: MediaAssetImporterImpl by lazy {
        MediaAssetImporterImpl(
            mediaMetadataRetriever = mediaMetadataRetriever,
            transactionRunner = transactionRunner,
            albumImporter = lazy { albumImporter },
            artistImporter = lazy { artistImporter },
            audioFileImporter = lazy { audioFileImporter },
            imageImporter = lazy { imageImporter },
        )
    }
}
