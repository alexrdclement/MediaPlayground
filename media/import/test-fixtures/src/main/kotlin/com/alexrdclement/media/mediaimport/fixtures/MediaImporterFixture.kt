package com.alexrdclement.media.mediaimport.fixtures

import com.alexrdclement.media.metadata.FakeMediaMetadataRetriever
import com.alexrdclement.media.store.FakeFileWriter
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporterImpl

class MediaImporterFixture(
    val mediaMetadataRetriever: FakeMediaMetadataRetriever = FakeMediaMetadataRetriever(),
    val fileWriter: FakeFileWriter = FakeFileWriter()
) {
    val mediaImporter = MediaImporterImpl(
        mediaMetadataRetriever = mediaMetadataRetriever,
        fileWriter = fileWriter,
    )
}
