package com.alexrdclement.mediaplayground.media.model

import kotlin.time.Instant

val FakeArtist1 = Artist(
    id = ArtistId("1"),
    name = "Speedy Ortiz",
    notes = null,
    createdAt = Instant.DISTANT_PAST,
    modifiedAt = Instant.DISTANT_PAST,
)

val FakeArtist2 = Artist(
    id = ArtistId("2"),
    name = "IDLES",
    notes = null,
    createdAt = Instant.DISTANT_PAST,
    modifiedAt = Instant.DISTANT_PAST,
)
