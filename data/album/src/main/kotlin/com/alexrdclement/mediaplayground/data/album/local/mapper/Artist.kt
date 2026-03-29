package com.alexrdclement.mediaplayground.data.album.local.mapper

import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import com.alexrdclement.mediaplayground.database.model.Artist as ArtistEntity

fun ArtistEntity.toSimpleArtist(): SimpleArtist {
    return SimpleArtist(
        id = id,
        name = name,
    )
}
