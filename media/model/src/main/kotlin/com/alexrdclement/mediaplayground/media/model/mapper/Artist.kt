package com.alexrdclement.mediaplayground.media.model.mapper

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import java.util.UUID

fun MediaMetadata.Audio.toArtist(name: String): Artist = Artist(
    id = ArtistId(UUID.randomUUID().toString()),
    name = name,
    notes = null,
)
