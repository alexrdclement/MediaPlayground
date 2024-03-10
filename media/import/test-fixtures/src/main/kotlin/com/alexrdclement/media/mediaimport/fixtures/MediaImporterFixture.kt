package com.alexrdclement.media.mediaimport.fixtures

import com.alexrdclement.media.mediaimport.fakes.FakeFileWriter
import com.alexrdclement.media.mediaimport.fakes.FakeMediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter

class MediaImporterFixture {
    val mediaMetadataRetriever = FakeMediaMetadataRetriever()
    val fileWriter = FakeFileWriter()

    val mediaImporter = MediaImporter(
        mediaMetadataRetriever = mediaMetadataRetriever,
        fileWriter = fileWriter,
    )
}
