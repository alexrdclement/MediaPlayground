package com.alexrdclement.mediaplayground.data.audio.local.mapper

import com.alexrdclement.mediaplayground.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.database.model.Artist as ArtistEntity

fun SimpleArtist.toArtistEntity(): ArtistEntity {
    return ArtistEntity(
        id = id,
        name = name,
    )
}

fun ArtistEntity.toSimpleArtist(): SimpleArtist {
    return SimpleArtist(
        id = id,
        name = name,
    )
}
