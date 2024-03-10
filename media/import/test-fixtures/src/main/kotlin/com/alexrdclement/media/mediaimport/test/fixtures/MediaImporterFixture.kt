package com.alexrdclement.media.mediaimport.test.fixtures

import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter

class MediaImporterFixture {
    val mediaMetadataRetriever = FakeMediaMetadataRetriever()
    val fileWriter = FakeFileWriter()

    val mediaImporter = MediaImporter(
        mediaMetadataRetriever = mediaMetadataRetriever,
        fileWriter = fileWriter,
    )
}
