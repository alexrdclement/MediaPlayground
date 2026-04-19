package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.CompleteAlbum

val FakeCompleteAlbum1 = CompleteAlbum(
    simpleAlbum = FakeSimpleAlbum1,
    tracks = listOf(
        FakeCompleteTrack1,
        FakeCompleteTrack2,
    )
)

val FakeCompleteAlbum2 = FakeCompleteAlbum1.copy(
    simpleAlbum = FakeSimpleAlbum2,
    tracks = listOf(
        FakeCompleteTrack3,
    )
)
