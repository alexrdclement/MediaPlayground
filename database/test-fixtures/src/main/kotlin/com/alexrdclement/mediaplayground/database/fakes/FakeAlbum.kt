package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Album
import com.alexrdclement.mediaplayground.database.model.Source
import kotlin.time.Instant

val FakeAlbum1 = Album(
    id = "1",
    title = "Album 1",
    modifiedDate = Instant.DISTANT_PAST,
    source = Source.Local,
)

val FakeAlbum2 = FakeAlbum1.copy(
    id = "2",
    title = "Album 2",
    source = Source.Local,
)
