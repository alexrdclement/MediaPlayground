package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.model.Artist

val FakeArtist1 = Artist(
    id = "1",
    name = "Artist 1",
)

val FakeArtist2 = FakeArtist1.copy(
    id = "2",
    name = "Artist 2",
)
