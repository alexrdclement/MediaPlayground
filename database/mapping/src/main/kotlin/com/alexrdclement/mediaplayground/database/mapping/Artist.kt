package com.alexrdclement.mediaplayground.database.mapping

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.database.model.Artist as ArtistEntity

fun Artist.toArtistEntity(): ArtistEntity {
    return ArtistEntity(
        id = id.value,
        name = name,
        notes = notes,
    )
}

fun ArtistEntity.toArtist(): Artist {
    return Artist(
        id = ArtistId(id),
        name = name,
        notes = notes,
    )
}
