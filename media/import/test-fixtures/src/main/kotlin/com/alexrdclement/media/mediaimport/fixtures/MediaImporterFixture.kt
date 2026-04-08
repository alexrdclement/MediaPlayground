package com.alexrdclement.media.mediaimport.fixtures

import com.alexrdclement.media.metadata.FakeMediaMetadataRetriever
import com.alexrdclement.media.store.FakeFileWriter
import com.alexrdclement.mediaplayground.media.mediaimport.ImageImporterImpl
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.store.ImageMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import com.alexrdclement.mediaplayground.media.store.PathProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.io.files.Path

class MediaImporterFixture(
    val mediaMetadataRetriever: FakeMediaMetadataRetriever = FakeMediaMetadataRetriever(),
    val fileWriter: FakeFileWriter = FakeFileWriter()
) {
    val imageImporter = ImageImporterImpl(
        mediaMetadataRetriever = mediaMetadataRetriever,
        fileWriter = fileWriter,
        pathProvider = object : PathProvider {
            override fun getAlbumDir(albumId: AlbumId): Path = Path("/tmp/albums/${albumId.value}")
            override fun getImagesDir(): Path = Path("/tmp/images")
            override fun getImagePath(imageId: ImageId, extension: String): Path =
                Path("/tmp/images/${imageId.value}.$extension")
        },
        imageMediaStore = object : ImageMediaStore {
            override fun getImageFlow(imageId: ImageId): Flow<Image?> = flowOf(null)
            context(scope: MediaStoreTransactionScope)
            override suspend fun put(images: Set<Image>) {}
        },
        transactionRunner = object : MediaStoreTransactionRunner {
            override suspend fun <T> run(block: suspend MediaStoreTransactionScope.() -> T): T {
                val scope = object : MediaStoreTransactionScope {}
                return block(scope)
            }
        },
    )
}
